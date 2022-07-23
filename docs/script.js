const input = document.getElementById('input')
const button = document.getElementById('button')

button.addEventListener('click', () => {
    if (!isNumber(input.value)) {
        return
    }
    window.open(`https://api.whatsapp.com/send/?phone=%2B91${input.value}&text&app_absent=0`,"_self")
})

function isNumber(number) {
    return number.length == 10 && !isNaN(number) && !isNaN(parseFloat(number))
}