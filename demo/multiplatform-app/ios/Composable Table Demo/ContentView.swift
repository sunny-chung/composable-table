//
//  ContentView.swift
//  Composable Table Demo
//
//  Created by Sunny Chung on 11/2/2024.
//

import SwiftUI
import ComposableTableDemoShared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        MainViewControllerKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // no-op
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard)
    }
}

#Preview {
    ContentView()
}
