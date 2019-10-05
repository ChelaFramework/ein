package ein.android.view.viewmodel.prop

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*
import android.widget.Toast

class WebClient(val view:View, val listener:WebClientListener = WebClientListener.EMPTY):WebViewClient(){
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed()
        super.onReceivedSslError(view, handler, error)
    }
    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        return checkUrl(url)
    }
    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?) = checkUrl(url)
    private fun checkUrl(url: String?): Boolean {
        if(url == null) return false
        if(url.startsWith("tel:")) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
            view.context.startActivity(intent)
            return true
        }
        if(url.startsWith("mailto:")) {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
            view.context.startActivity(intent)
            return true
        }
        listener.urlChanged(url)
        return false
    }
    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        if (error != null) receivedError(error.errorCode)
        super.onReceivedError(view, request, error)
    }
    private fun receivedError(errorCode: Int) {
        Toast.makeText(view.context, "weberror: ${webviewError(errorCode)}", Toast.LENGTH_LONG).show()
    }
    private fun webviewError(errorCode: Int): String {
        var result = ""
        when (errorCode) {
            ERROR_AUTHENTICATION-> result = "서버에서 사용자 인증 실패"
            ERROR_BAD_URL-> result = "잘못된 URL"
            ERROR_CONNECT-> result = "서버로 연결 실패"
            ERROR_FAILED_SSL_HANDSHAKE-> result = "SSL handshake 수행 실패"
            ERROR_FILE-> result = "일반 파일 오류"
            ERROR_FILE_NOT_FOUND-> result = "파일을 찾을 수 없습니다"
            ERROR_HOST_LOOKUP-> result = "서버 또는 프록시 호스트 이름 조회 실패"
            ERROR_IO-> result = "서버에서 읽거나 서버로 쓰기 실패"
            ERROR_PROXY_AUTHENTICATION-> result = "프록시에서 사용자 인증 실패"
            ERROR_REDIRECT_LOOP-> result = "너무 많은 리디렉션"
            ERROR_TIMEOUT-> result = "연결 시간 초과"
            ERROR_TOO_MANY_REQUESTS-> result = "페이지 로드중 너무 많은 요청 발생"
            ERROR_UNKNOWN-> result = "일반 오류"
            ERROR_UNSUPPORTED_AUTH_SCHEME-> result = "지원되지 않는 인증 체계"
            ERROR_UNSUPPORTED_SCHEME-> result = "ERROR_UNSUPPORTED_SCHEME"
        }
        return result
    }
}