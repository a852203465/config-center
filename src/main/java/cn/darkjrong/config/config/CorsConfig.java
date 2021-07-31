package cn.darkjrong.config.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * 跨域请求配置
 * @date 2021/06/01
 * @author Rong.Jia
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig {

   @Bean
    protected CorsFilter  corsFilter() {

       CorsConfiguration corsConfiguration = new CorsConfiguration();
       //1,允许任何来源
       corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
       //2,允许任何请求头
       corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
       //3,允许任何方法
       corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
       //4,允许凭证
       corsConfiguration.setAllowCredentials(true);

       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", corsConfiguration);

       return new CorsFilter(source);
    }
}
