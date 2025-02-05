from PyQt5 import QtWidgets, QtWebEngineWidgets, QtCore
import sys

class WebViewerApp(QtWidgets.QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle('Web Viewer Application')
        self.setGeometry(100, 100, 1200, 800)

        # Create a web engine view to display the website
        self.browser = QtWebEngineWidgets.QWebEngineView()
        self.browser.setUrl(QtCore.QUrl("https://index-education.net/pronote/"))
        
        # Set up the layout
        self.central_widget = QtWidgets.QWidget()
        self.setCentralWidget(self.central_widget)
        self.layout = QtWidgets.QVBoxLayout()
        self.central_widget.setLayout(self.layout)
        
        # Create buttons
        self.home_button = QtWidgets.QPushButton("Home")
        self.reload_button = QtWidgets.QPushButton("Reload")
        
        # Connect buttons to their functions
        self.home_button.clicked.connect(self.go_home)
        self.reload_button.clicked.connect(self.reload_page)
        
        # Add buttons to the layout
        self.layout.addWidget(self.home_button)
        self.layout.addWidget(self.reload_button)
        
        # Add the browser to the layout
        self.layout.addWidget(self.browser)

    def go_home(self):
        self.browser.setUrl(QtCore.QUrl("https://index-education.net/pronote/"))

    def reload_page(self):
        self.browser.reload()

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    main_window = WebViewerApp()
    main_window.show()
    sys.exit(app.exec_())
