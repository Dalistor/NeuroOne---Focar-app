import SwiftUI
import WebKit

struct WebView: UIViewRepresentable {
    let url: URL

    func makeUIView(context: Context) -> WKWebView {
        let configuration = WKWebViewConfiguration()
        // Permitir reprodução de mídia inline se necessário
        configuration.allowsInlineMediaPlayback = true
        
        let webView = WKWebView(frame: .zero, configuration: configuration)
        // Opcional: configurar delegate se quiser tratar navegação
        // webView.navigationDelegate = context.coordinator
        
        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {
        let request = URLRequest(url: url)
        uiView.load(request)
    }
}

struct ContentView: View {
    // Mesma URL usada no TWA do Android
    let targetUrl = URL(string: "https://test-focar-remake.biohealth.com.br")!
    
    var body: some View {
        WebView(url: targetUrl)
            .ignoresSafeArea() // Ocupa toda a tela, como no modo Imersivo do TWA
    }
}
