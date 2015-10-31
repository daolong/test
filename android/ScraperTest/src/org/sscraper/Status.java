/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper;

public class Status {    
    public final static int OK          = 1000; // get the result
    public final static int NG          = 1001; // fail to get result
    public final static int BAD_PARAM   = 1002; // bad parameter
    public final static int NOT_FOUND   = 1003; // could not find information
    public final static int AUTH_FAIL   = 1004; // permission denied
    public final static int OUT_OF_DATE = 1005; // authorization out of date
    public final static int FORMAT_ERROR = 1006; // 
}
