function loadNavbar() {
    fetch('navbar.html')
      .then(response => response.text())
      .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data;
      })
      .catch(error => console.error('Error loading navbar:', error));
  }
  
  function toggleNavbar() {
    const navbarNav = document.getElementById('navbarNav');
    navbarNav.style.display = navbarNav.style.display === 'flex' ? 'none' : 'flex';
  }
  
  // Carica la navbar al caricamento della pagina
  document.addEventListener('DOMContentLoaded', loadNavbar);
  