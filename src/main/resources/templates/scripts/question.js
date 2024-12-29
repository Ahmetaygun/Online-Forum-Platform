// API URL'si
const API_URL = "http://localhost:8087/users";

// Soruları listelemek için fonksiyon
async function fetchQuestions() {
    const response = await fetch(`${API_URL}/questions`);
    if (response.ok) {
        const questions = await response.json();
        const questionsListDiv = document.getElementById("questions-list");
        questionsListDiv.innerHTML = ""; // Önceki soruları temizle

        // Soruları listele
        if (questions.length > 0) {
            questions.forEach(question => {
                const questionElement = document.createElement("div");
                questionElement.innerHTML = `<h3>${question.title}</h3><p>${question.content}</p><hr>`;
                questionsListDiv.appendChild(questionElement);
            });
        } else {
            questionsListDiv.innerHTML = "<p>Henüz soru yok.</p>";
        }
    } else {
        alert("Soruları yüklerken bir hata oluştu!");
    }
}

// Yeni soru eklemek için formu dinleyen işlev
document.getElementById("questionForm")?.addEventListener("submit", async (e) => {
    e.preventDefault(); // Sayfanın yenilenmesini engelle

    const title = document.getElementById("title").value;
    const content = document.getElementById("content").value;

    // Verilerin doğruluğunu kontrol et
    if (!title || !content) {
        alert("Başlık ve içerik boş olamaz!");
        return;
    }

    const response = await fetch(`${API_URL}/questions`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, content }),
    });

    if (response.ok) {
        alert("Soru başarıyla eklendi!");
        window.location.href = "questions.html"; // Sorular sayfasına yönlendir
    } else {
        const error = await response.text();
        alert("Soru eklenirken bir hata oluştu: " + error);
    }
});

// Sayfa yüklendiğinde soruları listele
if (document.getElementById("questions-list")) {
    fetchQuestions();
}
