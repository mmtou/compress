package com.mmtou.tool.compress;

/**
 * 参数错误异常
 * 
 * @author mmtou
 *
 */
public class ParamErrorException extends Exception {

  private static final long serialVersionUID = -6402364334148892265L;

  public ParamErrorException(String message) {
    super(message);
  }

}
