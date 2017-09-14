package com.amazonaws.lambda.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.lambda.demo.dto.BaseInfo;
import com.amazonaws.lambda.demo.dto.User;
import com.amazonaws.lambda.demo.util.BaseInfoRecorder;
import com.amazonaws.lambda.demo.util.TokenUtil;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<BaseInfo, String> {

	@Override
	public String handleRequest(BaseInfo input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("[" + new Date() + "]Input: " + JSONObject.toJSONString(input));
		String token = input.getToken();
		logger.log("[" + new Date() + "]token: " + token);
		User user = TokenUtil.validateToken(token);
		Map<String, Object> map = new HashMap<>();
		if (user == null) {
			map.put("success", false);
			map.put("userName", "");
		} else {
			map.put("success", true);
			map.put("userId", user.getUserId());
			map.put("userName", user.getUserName());
		}
		logger.log("[" + new Date() + "]isLogin:" + map);
		// 访问信息记录
		BaseInfoRecorder.record(user == null ? "unknow" : user.getUserId().toString(), input,
				"[" + new Date() + "]checkLogin:" + JSONObject.toJSONString(map));
		return JSONObject.toJSONString(map);
	}

}
