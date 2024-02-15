package com.tb.api.tbapiserver.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(getJasyptPassword());
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
  }

  private String getJasyptPassword() {
    try (InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jasypt_password.txt"));
      Scanner scanner = new Scanner(inputStream)) {
      return scanner.useDelimiter("\\A").next();
    } catch (IOException e) {
        throw new RuntimeException("Failed to read Jasypt password", e);
    }
  }
  
}
