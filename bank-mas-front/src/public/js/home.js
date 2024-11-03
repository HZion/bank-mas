document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem('token');
  
    if (!token) {
      console.error('토큰이 없습니다. 로그인 페이지로 리디렉션합니다.');
      window.location.href = '/login';
      return;
    }
  
    fetch('/api/users', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('네트워크 응답에 문제가 있습니다.');
        }
        return response.json();
      })
      .then(data => {
        const usernameElement = document.getElementById('username');
        if (usernameElement) {
          usernameElement.textContent = data.username;
        }
      })
      .catch(error => {
        console.error('유저 정보를 가져오는 도중 문제가 발생했습니다:', error);
      });
  });
  