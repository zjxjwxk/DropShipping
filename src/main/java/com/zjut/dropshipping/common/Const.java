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

    public interface Status {

        String NORMAL = "正常";

        String UNREVIEWED = "未审核";

        String FROZEN = "冻结";
    }

    public interface UploadType {

        String IDENTITY = "IDCard";

        String IDENTITY_CARD_1 = "IDCard-1";

        String IDENTITY_CARD_2 = "IDCard-2";
    }

}
