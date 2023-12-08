package cn.com.liurz.util.swagger;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

/**
 * 启动后访问：
 *
 * swagger（xiaoymin版本）
 *
 * http://localhost:8080/doc.html
 *
 *
 * # swagger配置
 * swagger:
 *   enable: true  #是否开启
 *   base:
 *     package: com.sinosoft.springbootplus    #扫描路径
 *   contact:
 *     email: zhangsan@168.com
 *     name: 张三
 *     url: ''
 *   description: ''
 *   title:
 *   url: ''
 *   version: 1.0
 *
 *   主要配配置：
 *   swagger:
 *     enable: true  #是否开启
 *     base:
 *       package: com.sinosoft.springbootplus    #扫描路径
 */
//@Configuration
//@EnableSwagger2
//@ConditionalOnProperty(name = {"swagger.enable"}, havingValue = "true",matchIfMissing = true)
public class Swagger2Config {
    // 标题
    @Value("${swagger.title}")
    private String title;
    // 扫描路径
    @Value("${swagger.base.package}")
    private String basePackage;
    // 描述
    @Value("${swagger.description}")
    private String description;
    // 版本号
    @Value("${swagger.version}")
    private String version;

    // 是否显示
    @Value("${swagger.enable}")
    private boolean enable;

    public Swagger2Config() {
    }

    @Bean
    public Docket createRestApi() {
        Docket build = (new Docket(DocumentationType.SWAGGER_2)).apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.basePackage(this.basePackage)).paths(PathSelectors.any()).build().globalOperationParameters(this.setHeaderToken());
        build.enable(Boolean.TRUE.equals(enable) ? Boolean.TRUE : Boolean.FALSE);
        return build;
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder()).title(this.title).description(this.description).version(this.version).build();
    }

    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList();
        String testTokenValue = "";
        ParameterBuilder tokenPar = new ParameterBuilder();
        Parameter tokenParameter = tokenPar.name("token").description("Token Request Header").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(testTokenValue).build();
        pars.add(tokenParameter);
        return pars;
    }
}
