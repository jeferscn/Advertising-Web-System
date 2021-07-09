let usuarios = [
    { login: "pedro", pass: "1234" },
    { login: "lara", pass: "799" },
    { login: "joao", pass: "852" },
    { login: "maria", pass: "456" },
]

const btnValida = document.getElementById('btn-submit')
const nome = document.getElementById('recipient-name')
const senha = document.getElementById('recipient-password')
const formulario = document.getElementById('form')

btnValida.addEventListener('click', validaEntrada)

function validaEntrada(alerta) {
    console.log("Tamanho da coleção de objetos: " + usuarios.login)

    for (let i = 0; i < usuarios.length; i++) {
        console.log(usuarios[i].login)
        console.log(nome.value)
        if (usuarios[i].login == nome.value && usuarios[i].pass == senha.value) {
            alert('Validado com sucesso!')
            formulario.action = "paginaUsuario.html"
            return true
        }
    }
    alert("Dados incorretos! Tente novamente.")
    alerta.preventDefault()
    nome.value = ""
    senha.value = ""
    nome.focus()
    return false
}

function sairLogin() {
    if (confirm("Você realmente deseja sair?")) {
        location.href = "index.html"
    }
    else {
        alert("Que bom que você ficou")
    }
}