package cn.com.liurz.util.http.test;

import cn.com.liurz.util.http.constant.RequestTypeEnum;
import cn.com.liurz.util.http.vo.HttpRequestInf;
import cn.com.liurz.util.http.vo.Request;


import java.util.HashMap;
import java.util.Map;

public class HttpParamTest implements HttpRequestInf<String> {
    @Override
    public Request request() {
        Request request = new Request();
        request.setUri("https://devpress-api.csdn.net/api/internal/blog/nsInfo/blog/55259170");
        request.setType(RequestTypeEnum.get);
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("cookie","uuid_tt_dd=10_28763459920-1661582551779-333723; Hm_up_6bcd52f51e9b3dce32bec4a3997715ac=%7B%22islogin%22%3A%7B%22value%22%3A%220%22%2C%22scope%22%3A1%7D%2C%22isonline%22%3A%7B%22value%22%3A%220%22%2C%22scope%22%3A1%7D%2C%22isvip%22%3A%7B%22value%22%3A%220%22%2C%22scope%22%3A1%7D%7D; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=6525*1*10_28763459920-1661582551779-333723; __gads=ID=e0809258ef8a6352-22373373d6d5006c:T=1661582554:RT=1661582554:S=ALNI_MZSAXR3ntmiW5ASYMDOPjbFRirqLg; c-login-auto=1; __bid_n=183ffc604e75893b9a4207; c_dl_um=-; c_first_ref=www.baidu.com; c_segment=11; dc_sid=96e6d6d713e1a1a6c52cd95d60eda44a; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1668311463,1668682402,1668758377,1668834477; hide_login=1; firstDie=1; __gpi=UID=0000091f270038ee:T=1661582554:RT=1668834476:S=ALNI_MZQ5Z5_RqGWKanUAdJVLtdE4w6Diw; unlogin_scroll_step=1668834492652; SESSION=588a8620-c617-4f7c-964e-d4366c8860be; ssxmod_itna=iqGxgDRDcDBD2BKGkDz=CW50K=wxIO70Bw7mSuYqGN5P3DZDiqAPGhDC++zGhG4xhPe7YOp44sRmGid+=XnWr0a=Vhx0aDbqGksi7h4GG0xBYDQxAYDGDDPDo2PD1D3qDkD7O1lS9kqi3DbO=Di4D+FLQDmqG0DDtOn4G2D7UcnYAcQWdPETbg/0iYFqDMAeGXWBW4FurWapDZQQWY4CpRDB=CxBQMAkNUmeDHCwXM4eAxFxhxAB5e7G4P8gq570G4/iwIX0hXm7k4/g4tU7xP8KwLCKDA/YSLeD; ssxmod_itna2=iqGxgDRDcDBD2BKGkDz=CW50K=wxIO70Bw7mSuDA=cNtD/iYnDFxhOG9aMchGKidKj=BpmG3AgirXsDOinYbplt4lBIzY4+p5+6Ha3gcGNx=MGxO5n/pZLbfbxR927Lou4ZEbcnqIGb9Z4iIxnFOZyhRZvkfgW1QE7tt7DhgiyNRiAhr7wsK4apXBeFNzIbQge8lBe+w6f7mKe+fbLj2tALR6LUAt8UP29+Ecu6PqOffD07el4DLxG7eYD==; c_dl_prid=1668758493533_764757; c_dl_rid=1668841191252_747849; c_dl_fref=https://www.baidu.com/link; c_dl_fpage=/download/qq_36462452/85055113; c_pref=https%3A//www.baidu.com/link; c_first_page=https%3A//blog.csdn.net/taozhexuan123/article/details/73739960; log_Id_click=219; log_Id_view=1412; dc_session_id=10_1668848999970.512131; c_ref=https%3A//blog.csdn.net/ouyang111222/article/details/78884634; c_dsid=11_1668849005589.316515; c_utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-55259170-blog-78884634.pc_relevant_multi_platform_whitelistv3; c_utm_relevant_index=2; c_page_id=default; dc_tos=rll84t; log_Id_pv=251; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1668849006");
        request.setHeader(headers);
        return request;
    }

    @Override
    public String format(String var1) {
        return var1;
    }

    @Override
    public boolean varify(String var1) {
        return true;
    }
}
