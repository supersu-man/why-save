const input = document.getElementById('input')
const button = document.getElementById('button')
const toggle = document.getElementById('themeSwitch')
const body = document.body

if (localStorage.getItem('dark-theme')) {
    body.classList.remove('light-theme')
    body.classList.add('dark-theme')
    toggle.setAttribute('checked', 'true')
}

toggle.addEventListener('change', () => {
    if (toggle.checked) {
        body.classList.remove('light-theme')
        body.classList.add('dark-theme')
        localStorage.setItem('dark-theme', true)
    } else {
        body.classList.add('light-theme')
        body.classList.remove('dark-theme')
        localStorage.removeItem('dark-theme')
    }
})

button.addEventListener('click', () => {
    if (!isNumber(input.value)) {
        return
    }
    window.open(`https://api.whatsapp.com/send/?phone=%2B91${input.value}&text&app_absent=0`, "_self")
})

function isNumber(number) {
    return number.length == 10 && !isNaN(number) && !isNaN(parseFloat(number))
}