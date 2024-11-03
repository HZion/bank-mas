const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('errorMessage');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('localhost:38082/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const data = await response.json();

        if (response.ok) {
            // 로그인 성공 시 토큰 저장
            if (data.data && data.data.token) {
                localStorage.setItem('token', data.data.token);
            }
            
            // 리다이렉트
            if (data.redirectUrl) {
                window.location.href = data.redirectUrl;
            } else {
                window.location.href = '/home';
            }
        } else {
            // 로그인 실패 시
            errorMessage.textContent = data.message || 'Invalid username or password.';
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        console.error('Error:', error);
        errorMessage.textContent = '로그인 처리 중 오류가 발생했습니다.';
        errorMessage.style.display = 'block';
    }
});