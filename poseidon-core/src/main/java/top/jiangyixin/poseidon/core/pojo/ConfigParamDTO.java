package top.jiangyixin.poseidon.core.pojo;

import java.util.List;

/**
 * @author jiangyixin
 */
public class ConfigParamDTO {
    private String accessToken;
    private String env;
    private List<String> keys;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "ConfigParamDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", env='" + env + '\'' +
                ", keys=" + keys +
                '}';
    }
}
