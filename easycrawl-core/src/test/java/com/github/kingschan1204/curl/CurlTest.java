package com.github.kingschan1204.curl;

import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.helper.http.CURLHelper;
import com.github.kingschan1204.easycrawl.helper.json.JsonHelper;
import com.github.kingschan1204.easycrawl.task.EasyCrawl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * curl bash 转换测试
 */
@Slf4j
@DisplayName("curl bash转换")
public class CurlTest {
    String curlPost = """
            curl 'https://www.csindex.com.cn/csindex-home/exportExcel/security-industry-search-excel/CH?type__1773=mqjxRD0iBGiQD%2Fii5BKqAK0Q7D%3DaDCKnDLAeD' \\
              -X 'POST' \\
              -H 'Accept: application/json, text/plain, */*' \\
              -H 'Accept-Language: zh-CN,zh;q=0.9' \\
              -H 'Cache-Control: no-cache' \\
              -H 'Connection: keep-alive' \\
              -H 'Content-Type: application/json;charset=UTF-8' \\
              -H 'Cookie: tfstk=fodt_vmIVDmGgMkD1FMH0Q1wvagHMCLwsh87iijghHKpPUrGhcSiMKKy2oXjb1AvpnthfC16MpPvzERi7s0N_F5VG0moZsLw7skLDWlExegfuQoA8ilk7FzC31x3lb2YtI0X1itfC6ZCyMN_fStfdW_VA5_b1ZZIJZSCcs6f5wwCSaZbciNstXDd0RsuMB4fkUMujUVbGCGVBgEhAw7e9NBOVGT3GFddWOIW5CP530bBtBBlKrmROUJHfwC-MW1OedtOReoYwG9kz6sBclnh8dt9OtdnuA8pMGBW19UYaefAln6vL0FlW1vdeCpZukT6qGp5_E4-jeBJpT7Cdre1sLYkgTOs6uShUZKRyhEO40OkwYR8qgQ0C23L3-W1-MwBx0kTddMhJg0hS-yVpv7dq23L3-W18wIo-Rw439HF.; aliyungf_tc=6ab76cf2e72cdb22517b4c2f425bd67dbc0a1e0f29f14a8880ff1ad78018616e; acw_tc=ac11000117253546610135316e2dcb35884102c7c60d434a3679580ec7047f; Hm_lvt_f0fbc9a7e7e7f29a55a0c13718e9e542=1725354663; Hm_lpvt_f0fbc9a7e7e7f29a55a0c13718e9e542=1725354663; HMACCOUNT=4FCC4C845FF2961F; ssxmod_itna=YqfEYvqGx0x+gDl4C5AQG7GgDmxFkDB+qY2Fmq0=PDuiDiqG=0q0tqv3UevDlgjqiNDAxAPDqx0EYe12irmx33AGDxzeWjSmdH3B60LqUzr27PNXN1ex0aDbqGkLUgDFeDxhe0rD74irDD4DAC5D=xDrD04GKzYGDYPDh6rPDlKC44DaP4i3Df5DRPoD0omkDQFKqfDDBE0L3Wy3QBghUx8Pv4D1LCAqtDYD9pxDsrD23DBAPsj4dz7XVRoNSaOpDCKDjaxGDmeCv4RG=ROeLBcgrlnDQehatj+qQCiHLDwdI5dxVS433Y2x8hVj5Mtre0y8WDDGRx2346qK4D==; ssxmod_itna2=YqfEYvqGx0x+gDl4C5AQG7GgDmxFkDB+qY2Fmq0=PDuiDiqG=0q0tqv3UeD66vRCDDs5QD7pa3Fanhq7UC=YYGF8UnrrH3I4OERaU94LM2aTZb9C2x5xdp0rnb5A7gSXkHdzaOESNR73MWBfXY7fqFSYhm4p+AkXnznhEewfwGhR+mdtHnnU9U8T7QlfjAkDW3M3SY7bp3PQ2zqb2SlUy3cipjdfuUM3xq8=ftGhwAoA3cLj4nmonCATEjPQe1q6yIK8vd+DQabtPFkDRqSOEQKbLkltEDV=4WDp6Q/SaIvfiomXjoj6j6qVCWCzuwDm99n3+4Snm6nbsnmH8wnlbOtcGjR30na7ym3P8Au/QyrWD1ixieI87TiSmYHq1G2lURx9Pmj2bIPuzP+GnD9pV4bwROiby3Me1zbi9wubcbihxWfInw5EqqaAgmIz+R=1y2+8HXSDf5HUfT+YM3wnjw5ceNwba6aWmFFU+QDT3mfHL5XQvb3KFKPrgIdDmNZnrIWwjW7LfF=HZ7HWYvIIpm5IkPQwlf+LCskLHLWCOop2+F4pw6W4H7AeZIjSfwAHTZ8RRF+Za1Z87DGffb91iffYEuw+ytD07AAxQBU0CtP2oIWgy6Ut5SdooIqIdpPKq3PRCc1Me7PKqP42xnxYwuPLD7q0PRl3fH=jH/D4Z1rzNHx+xZnCLHhqGe=4Q0sP2RcxHGE1DoygrDQ0aEr0DqKe6bt7YehDD===' \\
              -H 'DNT: 1' \\
              -H 'Origin: https://www.csindex.com.cn' \\
              -H 'Pragma: no-cache' \\
              -H 'Referer: https://www.csindex.com.cn/en/indices/index-detail/000300' \\
              -H 'Sec-Fetch-Dest: empty' \\
              -H 'Sec-Fetch-Mode: cors' \\
              -H 'Sec-Fetch-Site: same-origin' \\
              -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36' \\
              -H 'sec-ch-ua: "Chromium";v="128", "Not;A=Brand";v="24", "Google Chrome";v="128"' \\
              -H 'sec-ch-ua-mobile: ?0' \\
              -H 'sec-ch-ua-platform: "Windows"' \\
              --data-raw '{"searchInput":"","pageNum":1,"pageSize":10,"sortField":null,"sortOrder":null}'
            """;


    @DisplayName("通过curl命令构建请求")
    @Test
    public void curlTest() {
        HttpRequestConfig config = new CURLHelper(curlPost).getConfig();
        System.out.println(JsonHelper.of(config).pretty());
        File file = new EasyCrawl<File>()
                .webAgent(WebAgent.defaultAgent().config(config).folder("C:\\temp\\"))
                .analyze(WebAgent::getFile)
                .execute();
        log.info("文件上名：{} 文件大小：{} kb", file.getName(), file.length() / 1024);
    }


    @DisplayName("curl代理ip测试")
    @Test
    public void proxyTest() {
        String curl = """
                curl 'https://myip.ipip.net/'
                -x 'http://localhost:27890'
                """;
        HttpRequestConfig config = new CURLHelper(curl).getConfig();
//        System.out.println(JsonHelper.of(config).pretty());
        String result = new EasyCrawl<String>()
                .webAgent(WebAgent.defaultAgent(config))
                .analyze(r -> r.getResult().body()).execute();
        System.out.println(result);
    }

    @DisplayName("curl代理ip测试")
    @Test
    public void curlGetTest() {
        String curl = """
                curl 'https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=&secid=1.000985&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&beg=0&end=20500101&smplmt=460&lmt=1000000&_=1728608136951' \\
                   -H 'accept: */*' \\
                   -H 'accept-language: zh-CN,zh;q=0.9' \\
                   -H 'cache-control: no-cache' \\
                   -H 'cookie: qgqp_b_id=25b0352482c49ebfb8db15946d573136; HAList=ty-1-000985-%u4E2D%u8BC1%u5168%u6307%2Cty-0-000001-%u5E73%u5B89%u94F6%u884C%2Cty-1-603777-%u6765%u4F0A%u4EFD; websitepoptg_api_time=1728608158717; st_si=14672063260840; st_pvi=54548857357044; st_sp=2024-03-28%2010%3A55%3A44; st_inirUrl=https%3A%2F%2Fxueqiu.com%2FS%2FSH563080; st_sn=2; st_psi=20241011085816140-113200302671-8926879779; st_asi=delete' \\
                   -H 'dnt: 1' \\
                   -H 'pragma: no-cache' \\
                   -H 'referer: https://quote.eastmoney.com/zs000985.html?jump_to_web=true' \\
                   -H 'sec-ch-ua: "Google Chrome";v="129", "Not=A?Brand";v="8", "Chromium";v="129"' \\
                   -H 'sec-ch-ua-mobile: ?0' \\
                   -H 'sec-ch-ua-platform: "Windows"' \\
                   -H 'sec-fetch-dest: script' \\
                   -H 'sec-fetch-mode: no-cors' \\
                   -H 'sec-fetch-site: same-site' \\
                   -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36'
                """;
        HttpRequestConfig config = new CURLHelper(curl).getConfig();
        System.out.println(JsonHelper.of(config).pretty());
        JsonHelper result = new EasyCrawl<JsonHelper>()
                .webAgent(WebAgent.defaultAgent(config))
                .analyze(WebAgent::getJson)
                .execute();
        System.out.println(result);
    }

    @DisplayName("sz")
    @Test
    public void tradeDateTest() {
        String curl = """
                curl 'https://www.szse.cn/api/report/exchange/onepersistenthour/monthList?month=${month}&v=${timestamp}' \\
                  -H 'Accept: */*' \\
                  -H 'Accept-Language: zh-CN,zh;q=0.9' \\
                  -H 'Cache-Control: no-cache' \\
                  -H 'Connection: keep-alive' \\
                  -H 'DNT: 1' \\
                  -H 'Pragma: no-cache' \\
                  -H 'Referer: https://www.szse.cn/disclosure/index.html' \\
                  -H 'Sec-Fetch-Dest: empty' \\
                  -H 'Sec-Fetch-Mode: cors' \\
                  -H 'Sec-Fetch-Site: same-origin' \\
                  -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36' \\
                  -H 'X-Requested-With: XMLHttpRequest' \\
                  -H 'sec-ch-ua: "Google Chrome";v="129", "Not=A?Brand";v="8", "Chromium";v="129"' \\
                  -H 'sec-ch-ua-mobile: ?0' \\
                  -H 'sec-ch-ua-platform: "Windows"'
                """;
        JsonHelper result = new EasyCrawl<JsonHelper>()
                .webAgent(curl)
                .args("month","2024-10")
                .analyze(WebAgent::getJson)
                .execute();
        System.out.println(result);
    }
}
