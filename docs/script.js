const input = document.getElementById('input')
const chatButton = document.getElementById('chatButton')
const downloadButton1 = document.getElementById('downloadButton1')
const downloadButton2 = document.getElementById('downloadButton2')

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

const ar = ['bruhh', 'really?', 'stop', 'yo?', 'ufff', 'bro?', 'all good?']

chatButton.addEventListener('click', () => {
    if (!isNumber(input.value)) {
        const randint = getRndInteger(0, ar.length - 1)
        alert(ar[randint])
        return
    }
    window.open(`https://api.whatsapp.com/send/?phone=%2B91${input.value}&text&app_absent=0`, '_self')
})

downloadButton1.addEventListener('click', () => {
    window.open('https://play.google.com/store/apps/details?id=com.supersuman.whysave', '_self')
})

downloadButton2.addEventListener('click', () => {
    window.open('https://github.com/supersu-man/why-save/releases', '_self')
})

function getRndInteger(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function isNumber(number) {
    return number.length == 10 && !isNaN(number) && !isNaN(parseFloat(number))
}