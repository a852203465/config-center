package cn.darkjrong.config.controller;

import cn.darkjrong.config.constants.FileConstant;
import cn.darkjrong.config.enums.ResponseEnum;
import cn.darkjrong.config.pojo.vo.ApplicationDTO;
import cn.darkjrong.config.pojo.vo.ApplicationVO;
import cn.darkjrong.config.pojo.vo.ResponseVO;
import cn.darkjrong.config.utils.FileUtils;
import cn.darkjrong.config.utils.PropertiesUtils;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置中心控制器
 * @date 2021/06/01
 * @author Rong.Jia
 */
@Slf4j
@RestController
@RequestMapping("/configCenter/application")
public class ApplicationController {

    @Autowired
    private NativeEnvironmentProperties nativeEnvironmentProperties;

    private static final Map<String, ApplicationVO> properties = new ConcurrentHashMap<>();

    /**
     * 添加yml文件路径
     * @param ymlFile 文件
     * @return
     */
    @PostMapping("")
    public ResponseVO addApplication(@RequestParam("file") MultipartFile ymlFile){

        if(ObjectUtil.isNull(ymlFile) || ymlFile.isEmpty()){
            return ResponseVO.error(ResponseEnum.FILE_NOT_NULL);
        }

        try{

            File file = new File(ymlFile.getOriginalFilename());
            FileUtil.writeBytes(IoUtil.readBytes(ymlFile.getInputStream()), file);

            if(!StrUtil.endWith(FileUtil.getName(file), FileConstant.YML_SUFFIX)){
                FileUtils.del(file);
                return ResponseVO.error(ResponseEnum.NOT_YML);
            }
            if(!PropertiesUtils.addFile(nativeEnvironmentProperties.getSearchLocations()[0], file)){
                FileUtils.del(file);
                return ResponseVO.error(ResponseEnum.FILE_EXISTS);
            }
            FileUtils.del(file);

            ApplicationVO applicationVO = getApplication(file);

            properties.put(applicationVO.getFileCode(), applicationVO);
        }catch (IOException e){
            log.error("addApplication {}", e.getMessage());
            return ResponseVO.error(ResponseEnum.UPLOAD_FAILED);
        }

        return ResponseVO.success();
    }

    /**
     * 删除文件
     * @param fileCode
     * @return
     */
   @DeleteMapping("/{code}")
   public ResponseVO deleteApplication(@PathVariable("code") String fileCode){

       if (properties.containsKey(fileCode)) {
           PropertiesUtils.deleteFile(nativeEnvironmentProperties.getSearchLocations()[0], properties.get(fileCode).getFileName());
            properties.remove(fileCode);
       }

       return ResponseVO.success();
   }

    /**
     * 获取全部yml文件信息
     * @return
     */
    @GetMapping("")
    public ResponseVO<List<ApplicationVO>> getApplications(){

        //yml文件,代表yml文件具体内容
        List<ApplicationVO> applicationVOList = new ArrayList<>();

        if (MapUtil.isEmpty(properties)) {
        //属性文件，代表yml文件的路径
            List<File> ymlFileList = PropertiesUtils.getProperties(nativeEnvironmentProperties.getSearchLocations()[0]);

            if (CollectionUtil.isNotEmpty(ymlFileList)) {
                for (File file:ymlFileList){
                    ApplicationVO applicationVO = getApplication(file);
                    properties.put(applicationVO.getFileCode(), applicationVO);
                    applicationVOList.add(applicationVO);
                }
            }
        }else {
            applicationVOList.addAll(properties.values());
        }

        return ResponseVO.success(applicationVOList);
    }

    @GetMapping("/{fileCode}")
    public ResponseVO<ApplicationVO> getApplication(@PathVariable("fileCode") String fileCode) {

        if (!properties.containsKey(fileCode)) {
            return ResponseVO.error(ResponseEnum.FILE_NOT_EXISTS);
        }

        ApplicationVO applicationVO = properties.get(fileCode);

        BufferedReader reader = null;
        try {

            File file = new File(applicationVO.getFilePath() + File.separator + applicationVO.getFileName());

            reader = new BufferedReader(new FileReader(file));
            //读取一行的字符串中转站
            String str;
            StringBuffer stringBuffer = new StringBuffer();
            while ((str=reader.readLine())!=null){
                stringBuffer.append(str+"\n");
            }
            applicationVO.setFileContent(stringBuffer.toString());
        }catch (Exception e) {
            log.error("getApplications {}", e.getMessage());
        }finally {
            IoUtil.close(reader);
        }

        return ResponseVO.success(applicationVO);
    }

    /**
     * 修改yml文件
     * @param applicationDTO
     * @return
     */
    @PutMapping("")
    public ResponseVO updateApplication(@RequestBody  ApplicationDTO applicationDTO){

        if (!properties.containsKey(applicationDTO.getFileCode())) {
            return ResponseVO.error(ResponseEnum.FILE_NOT_EXISTS);
        }

        String fileName = properties.get(applicationDTO.getFileCode()).getFileName();
        String fileContent = applicationDTO.getFileContent();
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PropertiesUtils.getFilePath(nativeEnvironmentProperties.getSearchLocations()[0], fileName)));
            bufferedWriter.write(fileContent);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("IO异常 {}", e.getMessage());
        }

        return ResponseVO.success();
    }

    private ApplicationVO getApplication(File file) {

        ApplicationVO applicationVO  = new ApplicationVO();
        applicationVO.setUpdatedTime(ObjectUtil.isNull(FileUtil.lastModifiedTime(file)) ? System.currentTimeMillis() : FileUtil.lastModifiedTime(file).getTime());
        applicationVO.setFileName(file.getName());
        applicationVO.setFilePath(nativeEnvironmentProperties.getSearchLocations()[0]);
        applicationVO.setFileCode(IdUtil.fastSimpleUUID());

        return applicationVO;
    }

















}
