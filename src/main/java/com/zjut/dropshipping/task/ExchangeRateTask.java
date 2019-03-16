package com.zjut.dropshipping.task;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.dataobject.ExchangeRate;
import com.zjut.dropshipping.repository.ExchangeRateRepository;
import com.zjut.dropshipping.utils.PropertiesUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zjxjwxk
 */
@Component
public class ExchangeRateTask {

    private static final String DEF_CHARSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private static final String ERROR_CODE = "error_code";
    private static final String RESULT = "result";

    private final ExchangeRateRepository exchangeRateRepository;

    private Logger logger = LoggerFactory.getLogger(ExchangeRateTask.class);

    @Autowired
    public ExchangeRateTask(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Scheduled(fixedRate = 86400000)
    public void update() {
        String result;
        // 请求接口地址
        String url = PropertiesUtil.getProperty("juhe.finance.exchange.rmbquot");
        // 请求参数
        Map<String, Object> params = new HashMap<>(2);
        // APP Key
        params.put("key", PropertiesUtil.getProperty("juhe.currency.exchange.rate.AppKEY"));
        // 两种格式(0或者1,默认为0)
        params.put("type", "");

        try {
            result = net(url, params, Const.RequestMethod.GET);
            JSONObject object = new JSONObject(result);
            if (object.getInt(ERROR_CODE) == 0){
                logger.info(object.get("result").toString());
                if (save(object.get(RESULT))) {
                    logger.info("更新外币汇率表成功");
                }
            } else {
                logger.debug(object.get("error_code") + ":" + object.get("reason"));
                logger.debug("更新外币汇率表失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean save(Object result) {

        JSONArray jsonArray = (JSONArray) result;
        Map<String, JSONObject> jsonObjectMap = (Map<String, JSONObject>) jsonArray.toList().get(0);
        for (Map.Entry<String, JSONObject> entry:
                jsonObjectMap.entrySet()) {
            Map<String, String> paramMap = (Map<String, String>) entry.getValue();
            String name = paramMap.get("name");
            Double bankConversionPri = Double.parseDouble(paramMap.get("bankConversionPri")) / 100;
            ExchangeRate exchangeRate = new ExchangeRate(name, bankConversionPri);
            if (exchangeRateRepository.save(exchangeRate) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     */
    private static String net(String strUrl, Map<String, Object> params, String method){
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (method == null || Const.RequestMethod.GET.equals(method)){
                strUrl = strUrl + "?" + urlEncode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || Const.RequestMethod.GET.equals(method)){
                conn.setRequestMethod(Const.RequestMethod.GET);
            } else {
                conn.setRequestMethod(Const.RequestMethod.POST);
                conn.setDoOutput(true);
            }
            String userAgent = PropertiesUtil.getProperty("userAgent");
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && Const.RequestMethod.POST.equals(method)) {
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlEncode(params));
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHARSET));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    /**
     * 将map型转为请求参数型
     * @param data map型请求
     * @return 参数型请求
     */
    private static String urlEncode(Map<String,Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
