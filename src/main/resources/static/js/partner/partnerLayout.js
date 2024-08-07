document.addEventListener('DOMContentLoaded', function() {
  const cmsubnav = document.querySelector('.cmsubnav');
  if (!cmsubnav) return; // cmsubnav가 없으면 함수 종료

  const links = cmsubnav.querySelectorAll('a');
  const currentPath = window.location.pathname;

  links.forEach(link => {
    if (link.getAttribute('href') === currentPath) {
      link.classList.add('active');
    }

    link.addEventListener('click', function(e) {
      // 실제 페이지 이동이 있다면 e.preventDefault()는 제거
      // e.preventDefault();

      links.forEach(l => l.classList.remove('active'));
      this.classList.add('active');
      localStorage.setItem('activeMenu', this.getAttribute('href'));
    });
  });

  // 페이지 로드 시 저장된 활성 메뉴 확인 및 적용
  const activeMenu = localStorage.getItem('activeMenu');
  if (activeMenu) {
    const activeLink = cmsubnav.querySelector(`a[href="${activeMenu}"]`);
    if (activeLink) {
      activeLink.classList.add('active');
    }
  }
});