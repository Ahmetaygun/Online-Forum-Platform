// API URL'si backend'in çalıştığı portu yansıtsın
const API_URL = "http://localhost:8087/users"; // Aynı port üzerinden API'ye erişim

// Kayıt formu işleme
document.getElementById("registerForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch(`${API_URL}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ username, password }),
    });

    if (response.ok) {
        alert("Kayıt başarılı!");
        window.location.href = "login.html"; // Başarı durumunda login sayfasına yönlendirme
    } else {
        alert("Kayıt başarısız!");
    }
});

// Giriş formu işleme
document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch(`${API_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ username, password }),
    });

    if (response.ok) {
        alert("Giriş başarılı!");
        window.location.href = "questions.html"; // Başarı durumunda sorular sayfasına yönlendirme
    } else {
        alert("Giriş başarısız!");
    }
});
