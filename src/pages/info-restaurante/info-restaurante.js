const btn_horario = document.getElementsByClassName("button-horario");
const modalHorario = document.getElementById("modal-horario")
const btnVerHorarios = document.querySelector("#btn-ver-horario")

for (let i = 0; i < btn_horario.length; i++) {

  (function (index) {
    btn_horario[index].addEventListener("click", function () {
      modalHorario.classList.add("mostrar")
    })
  })(i)
}

modalHorario.addEventListener("click", function (e) {
  if (e.target.className == modalHorario.className || e.target.id == "button-fechar") {
    modalHorario.classList.remove("mostrar")
  }

})

btnVerHorarios.addEventListener("click", () => {
  window.location.href = '../info-horarios/info-horarios.html'
})

