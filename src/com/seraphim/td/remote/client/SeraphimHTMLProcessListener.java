package com.seraphim.td.remote.client;

public interface SeraphimHTMLProcessListener {
   void startHTMLProcess();
   /**
    * 解析成功，返回URL
    * 解析失败，返回null；
    */
   void finishHTMLProcess(String url);
   void errorHTMLProcess(int errorCode);
}
