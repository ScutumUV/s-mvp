package com.superchen.demo.fragment.libTest1;

/**
 * Created by superchen on 2017/6/19.
 */

public class PublicKeyEntity {


    /**
     * status : 1
     * data : {"PublicKey":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7unneBpRpCVkFL+3u/svqN3/QNyt9m1RTZUaBsaoD776SGMbhungakqcFRIbeqcQumuWjHmUm/SbD4QuEMhCYg79dr/CIeF9uC7umRaI0UrspzgORIn4IPrWrVgt98KxBlJ+RF67VDWkbM3bA5aCG0MY9Yo09F1GPoE7CtYPmOQIDAQAB"}
     */

    private int status;
    /**
     * PublicKey : MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7unneBpRpCVkFL+3u/svqN3/QNyt9m1RTZUaBsaoD776SGMbhungakqcFRIbeqcQumuWjHmUm/SbD4QuEMhCYg79dr/CIeF9uC7umRaI0UrspzgORIn4IPrWrVgt98KxBlJ+RF67VDWkbM3bA5aCG0MY9Yo09F1GPoE7CtYPmOQIDAQAB
     */

    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String PublicKey;

        public String getPublicKey() {
            return PublicKey;
        }

        public void setPublicKey(String PublicKey) {
            this.PublicKey = PublicKey;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "PublicKey='" + PublicKey + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PublicKeyEntity{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
