package org.dante.springboot.thirdclient.prop;

import lombok.Data;

/**
 * 微信 OAuth 配置
 * 
 * https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319
 * 
 * @author dante
 *
 */
@Data
public class WechatProp {
	private String appId;
	private String appsecret;
}
