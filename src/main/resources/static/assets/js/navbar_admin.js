fetch('../components/navbar_admin.html')
  .then(res => res.text())
  .then(html => {
    document.getElementById('navbar').innerHTML = html;

    const tryInitMenu = () => {
      const toggle = document.querySelector(".user-toggle");
      const dropdown = document.getElementById("dropdownMenu");

      if (toggle && dropdown) {
        toggle.addEventListener("click", function (e) {
          e.stopPropagation();
          dropdown.style.display = (dropdown.style.display === "block") ? "none" : "block";
        });

        document.addEventListener("click", function (e) {
          if (!toggle.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = "none";
          }
        });
      } else {
        setTimeout(tryInitMenu, 50);
      }
    };

    tryInitMenu();
  });
