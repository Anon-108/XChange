package org.knowm.xchange.client;

import si.mazi.rescu.ClientConfig;

/**
 * 客户端配置定制器
 */
public interface ClientConfigCustomizer {

  /** 定制*/
  void customize(ClientConfig clientConfig);
}
