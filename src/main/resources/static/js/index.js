let url = "/configCenter/application"
var editor;

/**
 *  Table动态表格生成
 */
function showTable() {

    $('#table').bootstrapTable({
        ajax: function (request) {
            getData();
        }
    });
}

/**
 * 获取列
 */
function getColumns() {

    return [
        {
            title: '序号',
            field: "idx",
            align: 'center',
            halign: 'center',
            switchable: true,
            sortable: true,
            formatter: function (value, row, index) {
                return index + 1;
            }
        },
        {
            title: '文件编码',
            field: "fileCode",
            align: 'center',
            halign: 'center',
            switchable: true,
            sortable: true,
        },
        {
            title: '文件名',
            field: "fileName",
            align: 'center',
            halign: 'center',
            switchable: true,
            sortable: true,
        },
        {
            title: '文件路径',
            field: "filePath",
            align: 'center',
            halign: 'center',
            switchable: true,
            sortable: true,
        },
        {
            title: '更新时间',
            field: "updatedTime",
            align: 'center',
            halign: 'center',
            switchable: true,
            sortable: true,
            formatter: function (value, row, index) {
                return changeDateFormat(value)
            }
        },
        {
            title: '操作',
            field: 'button',
            events: operateEvents,
            formatter: addFunctionAlty
        }
    ];
}

/**
 *  时间戳转日期
 * @param timestamp 时间戳
 * @returns {string} 日期
 */
function changeDateFormat(timestamp) {
    var now = new Date(parseInt(timestamp));
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var date = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}

/**
 * 刷新table
 */
function refreshTable() {
    $("#table").bootstrapTable('refreshOptions', {
        ajax: function (request) {
            getData();
        }
    });
}

/**
 * 获取数据
 */
function getData() {
    get(url, null, function (json) {
        $('#table').bootstrapTable('destroy').bootstrapTable({
            data: json,
            toolbar: '#toolbar',
            cache: false,
            striped: true, //是否显示行间隔色
            sidePagination: "client",
            sortOrder: "desc",
            pageSize: 6,
            pageNumber: 1,
            showRefresh: true,   //是否显示刷新按钮
            pageList: [6, 20, 35, 50, 100],
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            showColumns: true, //选择要显示的列
            showExport: true,
            exportDataType: "basic",
            pagination: true,
            strictSearch: true,
            search: true,
            sortable: true,                     //是否启用排序
            paginationPreText: "上一页",
            paginationNextText: "下一页",
            minimumCountColumns: 2,             //最少允许的列数
            uniqueId: "fileCode",                     //每一行的唯一标识，一般为主键列
            showFooter: true,
            undefinedText: "",
            queryParamsType: "undefined",//设置参数格式
            multipleSearch: true,
            columns: getColumns(),
            onRefresh: refreshTable
        });
    })
}

/**
 * 添加操作
 * @param value
 * @param row
 * @param index
 */
function addFunctionAlty(value, row, index) {
    return [
        '<button id="tableDetails" type="button" class="btn btn-default">详情</button>&nbsp;&nbsp;',
        '<button id="tableEditor" type="button" class="btn btn-info">编辑</button>&nbsp;&nbsp;',
        '<button id="tableDelete" type="button" class="btn btn-danger">删除</button>'
    ].join('');
}

window.operateEvents = {
    // 详情
    "click #tableDetails": function (e, value, row, index) {
        $("#fileDetailsMadelLabel").text(row.fileName + " 详情");
        getRest(url + "/" + row.fileCode, function (json) {
            if (json && json.fileContent) {
                createEditor("codeDetails");
                editor.setReadOnly(true);
                editor.setValue(json.fileContent);
                exTheme(editor, "exThemeDetails");
                $("#fileDetailsMadelLabel").attr("fileCode", row.fileCode);
            }
        });

        $('#fileDetailsMadel').modal('show');
    },

    // 修改
    "click #tableEditor": function (e, value, row, index) {
        $("#fileEditMadelLabel").text(row.fileName + " 编辑");
        getRest(url + "/" + row.fileCode, function (json) {
            if (json && json.fileContent) {
                createEditor("codeEdit");
                editor.setValue(json.fileContent);
                exTheme(editor, "exThemeEdit");
                $("#fileEditMadelLabel").attr("fileCode", row.fileCode);
            }
        });

        $('#fileEditMadel').modal('show');
    },

    // 删除
    "click #tableDelete": function (e, value, row, index) {
        remove(url, row.fileCode, function (e) {
            console.log(e);
            if (e !== -1) {
                $('#table').bootstrapTable('removeByUniqueId', row.fileCode);
            }
        })
    }
}

function fileEditMadelSave() {
    put(url, {fileCode: $("#fileEditMadelLabel").attr("fileCode"),
            fileContent: editor.getValue()},
    function () {
        $('#fileEditMadel').modal('hide');
    });
}

function fileEditMadelClose() {
    $("#fileEditMadel").modal('hide');
    editor.setValue("");
}

function uploadFileClose() {
    $("#file-Portrait1").fileinput('clear');
}

function initFileInput() {
    //初始化上传控件的样式
    $('#file-Portrait1').fileinput({
        language: "zh", //设置语言
        uploadUrl: url, //上传的地址
        enctype: 'multipart/form-data',
        showUpload: true,  // 显示上传按钮
        showRemove: true, //显示移除按钮
        showPreview: true, //是否显示预览
        showCaption: true,//是否显示标题
        autoReplace: true,
        allowedFileExtensions: ["yml", "properties", "yaml"],
        theme: "fa",      // 主题设置
        initialPreviewAsData: true,
        dropZoneEnabled: true,          // 禁止点击预览区域进行文件上传操作
        maxFileCount: 1,                    // 最大上传为 1
        browseClass: "btn btn-primary", //按钮样式
        previewClass: "uploadPreview",
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        // initialPreview: [
        //     "< img src='http://xxx/1.png' class='file-preview-image' />",
        // ],
    })
        .on("change", function () {
            // 清除掉上次上传的图片
            $(".uploadPreview").find(".file-preview-frame:first").remove();
            $(".uploadPreview").find(".kv-zoom-cache:first").remove();
        })
        // .on("filebatchselected", function (e, files) {
        //     $(this).fileinput("upload");             // 文件选择完直接调用上传方法。
        // })
        .on("fileuploaded", function (e, data, previewiId, index) {       // 上传完成后的处理
            // 清除当前的预览图 ，并隐藏 【移除】 按钮
            $(e.target)
                .fileinput('clear')
                .fileinput('unlock')
            $(e.target)
                .parent()
                .siblings('.fileinput-remove')
                .hide()

            refreshTable();

            if (data.response.code !== 0) {
                alert(data.response.message);
            }
        });
}

function createEditor(el) {

    /**
     * 文本编辑框插件
     */
    editor = ace.edit(el);
    //设置编辑器样式
    editor.setTheme("ace/theme/twilight");
    //设置程序语言模式
    editor.session.setMode("ace/mode/yaml");
    //字体大小
    editor.setFontSize(18);
    //设置只读
    editor.setReadOnly(false);
    //设置打印线是否显示
    editor.setShowPrintMargin(false);
    //自动换行,设置为off关闭
    editor.setOption("wrap", "free");
    //以下部分是设置输入代码提示的，如果不需要可以不用引用ext-language_tools.js
    ace.require("ace/ext/language_tools");
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
    });

    editor.setHighlightActiveLine(true); //代码高亮
    editor.setShowPrintMargin(false);
    //editor.setTheme(&#39;ace/theme/solarized_dark&#39;); //引入模板
    editor.getSession().setUseWorker(false);
    editor.getSession().setUseWrapMode(true); //支持代码折叠
    //editor.getSession().setMode(&#39;ace/mode/javascript&#39;); //设置语言模式
    editor.selection.getCursor(); //获取光标所在行或列
    //editor.gotoLine(lineNumber); //跳转到行
    editor.session.getLength(); //获取总行数
    // editor.insert("Something cool");
    editor.getSession().setUseSoftTabs(true);

    editor.resize();
}

function exTheme(editor, el) {
    $("#" + el).change(function () {
        if (this.value == "深夜黑(默认)") {
            editor.setTheme("ace/theme/twilight");
        } else if (this.value == "粉字白") {
            editor.setTheme("ace/theme/clouds");
        } else if (this.value == "深海蓝") {
            editor.setTheme("ace/theme/cobalt");
        } else if (this.value == "污水黑") {
            editor.setTheme("ace/theme/dracula");
        } else if (this.value == "蓝字白") {
            editor.setTheme("ace/theme/dreamweaver");
        } else if (this.value == "青字白") {
            editor.setTheme("ace/theme/eclipse");
        } else {
            return;
        }
    });

}

function fileDetailsMadelClose() {
    editor.setValue("");
}



































