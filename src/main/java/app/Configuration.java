package app;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
@ConfigurationProperties("app.configuration")
public class Configuration {
    @Getter private Duration timeout;

    public void setTimeout(Duration timeout) {
        if (this.timeout != null)
            throw new IllegalAccessError();
        this.timeout = timeout;
    }
}
