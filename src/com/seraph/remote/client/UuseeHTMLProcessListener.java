package com.seraph.remote.client;

public interface UuseeHTMLProcessListener {
   void startHTMLProcess();
   /**
    * 解析成功，返回URL
    * 解析失败，返回null；
    */
   void finishHTMLProcess(String url);
   void errorHTMLProcess(int errorCode);
}
