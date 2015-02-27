package net.unicon.boot.test

import org.jasig.cas.client.authentication.AttributePrincipal
import org.jasig.cas.client.authentication.AuthenticationFilter
import org.jasig.cas.client.authentication.Saml11AuthenticationFilter
import org.jasig.cas.client.util.AssertionThreadLocalFilter
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter
import org.jasig.cas.client.validation.Saml11TicketValidationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest

@Controller
@EnableAutoConfiguration
class TestApplication {
    @Value('${local.serverName}')
    String localServerName

    @Value('${cas.serverUrlPrefix}')
    String casServerUrlPrefix

    @Bean
    @Profile('cas')
    FilterRegistrationBean casAuthenticationFilter() {
        new FilterRegistrationBean(
                filter: new AuthenticationFilter(),
                urlPatterns: ['/*'],
                order: 1,
                initParameters: [
                        'casServerLoginUrl': "${casServerUrlPrefix}/login".toString(),
                        'serverName'       : localServerName
                ]
        )
    }

    @Bean
    @Profile('saml')
    FilterRegistrationBean samlAuthenticationFilter() {
        new FilterRegistrationBean(
                filter: new Saml11AuthenticationFilter(),
                urlPatterns: ['/*'],
                order: 1,
                initParameters: [
                        'casServerLoginUrl': "${casServerUrlPrefix}/login".toString(),
                        'serverName'       : localServerName
                ]
        )
    }

    @Bean
    FilterRegistrationBean casValidationFilter() {
        new FilterRegistrationBean(
                filter: new Cas20ProxyReceivingTicketValidationFilter(),
                urlPatterns: ['/*'],
                order: 2,
                initParameters: [
                        'casServerUrlPrefix': casServerUrlPrefix,
                        'serverName'        : localServerName
                ]
        )
        
    }

    @Bean
    FilterRegistrationBean samlValidationFilter() {
        new FilterRegistrationBean(
                filter: new Saml11TicketValidationFilter(),
                urlPatterns: ['/*'],
                order: 2,
                initParameters: [
                        'casServerUrlPrefix': casServerUrlPrefix,
                        'serverName'        : localServerName
                ]
        )
    }

    @Bean
    FilterRegistrationBean httpServerRequestWrapperFilter() {
        new FilterRegistrationBean(
                filter: new HttpServletRequestWrapperFilter(),
                urlPatterns: ['/*'],
                order: 3
        )
    }

    @Bean
    FilterRegistrationBean assertionThreadLocalFilter() {
        new FilterRegistrationBean(
                filter: new AssertionThreadLocalFilter(),
                urlPatterns: ['/*'],
                order: 4
        )
    }

    @RequestMapping('/')
    String view(HttpServletRequest request, Model model) {
        AttributePrincipal principal = request.userPrincipal
        model.addAttribute('principal', principal)
        return 'attributes'
    }

    public static void main(String... args) {
        SpringApplication.run(TestApplication, args)
    }
}
