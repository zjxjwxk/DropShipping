package com.zjut.dropshipping.common;

/**
 * 常量类
 * @author zjxjwxk
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String PHONE = "phone";
    public static final String IDENTITY_NUMBER = "identityNumber";
    public static final String EXTERNAL_SHOP = "externalShop";

    public interface AgentState {

        String NORMAL = "正常";

        String UNREVIEWED = "未审核";

        String FROZEN = "冻结";
    }

    public interface AgreementState {

        String NORMAL = "正常";

        String AGENT_REQUEST = "代理发送请求";

        String PRODUCER_REQUEST = "厂商发送请求";
    }

    public interface AgreementResponse {

        String ACCEPT = "accept";

        String REFUSE = "refuse";
    }

    public interface UploadType {

        String IDENTITY = "IDCard";

        String IDENTITY_CARD_1 = "IDCard-1";

        String IDENTITY_CARD_2 = "IDCard-2";
    }

}
