import random
import logging
import tkinter as tk
from tkinter import scrolledtext, font, messagebox

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def generate_key():
    """Generates a single product key in the format XXXXX-XXXXX-XXXXX-XXXXX-XXXXX."""
    key = '-'.join(''.join(random.choices('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', k=5)) for _ in range(5))
    log_message(f"Generated Key: {key}")
    return key

def log_message(message):
    """Adds a message to the log display area."""
    log_display.config(state=tk.NORMAL)
    log_display.insert(tk.END, message + "\n")
    log_display.see(tk.END)  # Auto-scroll to the latest log
    log_display.config(state=tk.DISABLED)

def on_generate_button_click():
    """Handles the Generate button click."""
    generate_key()

def copy_selection(event=None):
    """Copies the selected text to the clipboard."""
    selected_text = log_display.get(tk.SEL_FIRST, tk.SEL_LAST)
    root.clipboard_clear()
    root.clipboard_append(selected_text)

def select_all(event=None):
    """Selects all text in the log area."""
    log_display.tag_add("sel", "1.0", tk.END)

# Extra Functionalities
def clear_log():
    """Clears the log display."""
    log_display.config(state=tk.NORMAL)
    log_display.delete(1.0, tk.END)
    log_display.config(state=tk.DISABLED)

def save_log():
    """Saves log content to a file."""
    with open("log_output.txt", "w") as file:
        file.write(log_display.get(1.0, tk.END))
    messagebox.showinfo("Save Log", "Log saved to log_output.txt")

# Setting up the main window
root = tk.Tk()
root.title("Customized Product Key Generator")
root.geometry("680x525")
root.configure(bg="#2e3440")

# Title Label
title_font = font.Font(family="Helvetica", size=18, weight="bold")
title_label = tk.Label(root, text="Product Key Generator", font=title_font, fg="#88c0d0", bg="#2e3440")
title_label.pack(pady=20)

# Subtitle Label
subtitle_font = font.Font(family="Helvetica", size=12, weight="normal")
subtitle_label = tk.Label(root, text="Click 'Generate Key' to create a unique product key", font=subtitle_font, fg="#d8dee9", bg="#2e3440")
subtitle_label.pack(pady=10)

# Custom Generate Button
button_font = font.Font(family="Helvetica", size=12, weight="bold")
generate_button = tk.Button(
    root,
    text="Generate Key",
    font=button_font,
    command=on_generate_button_click,
    bg="#88c0d0",
    fg="#2e3440",
    activebackground="#5e81ac",
    activeforeground="#eceff4",
    width=20,
    height=2,
    relief="groove",
    bd=2
)
generate_button.pack(pady=20)

# Customized Log Display Area
log_display = scrolledtext.ScrolledText(root, wrap=tk.WORD, width=70, height=15, font=("Courier", 10), bg="#3b4252", fg="#d8dee9")
log_display.config(state=tk.DISABLED)
log_display.pack(pady=10)

# Context Menu for Log Display
log_menu = tk.Menu(log_display, tearoff=0)
log_menu.add_command(label="Copy", command=copy_selection)
log_menu.add_command(label="Select All", command=select_all)
log_menu.add_command(label="Clear Log", command=clear_log)
log_menu.add_command(label="Save Log to File", command=save_log)

# Show context menu on right-click
def show_log_menu(event):
    log_menu.post(event.x_root, event.y_root)

log_display.bind("<Button-3>", show_log_menu)

# Footer Label
footer_label = tk.Label(root, text="© 2024 KeyGen Inc.", font=("Helvetica", 8), fg="#4c566a", bg="#2e3440")
footer_label.pack(pady=10)

# Run the application
root.mainloop()
