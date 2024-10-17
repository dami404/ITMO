function showForm(btn) {
    console.log(btn);
    btn.style.display = 'none';
    document.getElementById('form').style.display = 'flex';
}
// отправка формы с заказом
async function save_order(event,par){
    event.preventDefault()
    const formElement = document.querySelector(par);
    const formData = new FormData(formElement);
    const date = formData.get('date'); 
    const comment = formData.get('comment'); 
    const technique = formData.get('technique'); 
    let body = {
        date: date,
        comment: comment,
        technique: technique,
    };
    await fetch('/~s338923/isbd/backend.php', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json;charset=utf-8',
          'REQUEST_TYPE': 'create_order'
        },
        body: JSON.stringify(body)
      }).then((response) => {
        alert("Заказ создан!")
        location.reload()
      })
}

// отправка формы с отзывом
async function rate_order(event,par){
    event.preventDefault()
    const formElement = document.querySelector(par);
    const formData = new FormData(formElement);
    const rating = formData.get('rating');
    const comment = formData.get('comment');
    let body = {
        rating: rating,
        comment: comment,
    };
    await fetch('/~s338923/isbd/backend.php', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(body)
      }).then(() => {
        alert("Отзыв сохранен!")
        window.location.href = '/~s338923/isbd/index.php'
      });
}

// отправка формы с вопросом
async function qa(event,par){
    event.preventDefault()
    const formElement = document.querySelector(par);
    const formData = new FormData(formElement);
    const rating = formData.get('rating');
    const comment = formData.get('comment');
    let body = {
        rating: rating,
        comment: comment,
    };
    await fetch('/~s338923/isbd/backend.php', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(body)
      }).then(() => {
        alert("Вопрос отправлен!")
        location.reload()
      });
}

// отправка формы с подпиской
async function subscribe(event,par){
    event.preventDefault()
    const formElement = document.querySelector(par);
    const formData = new FormData(formElement);
    const start_date = formData.get('start_date');
    const finish_date = formData.get('finish_date');
    const technique = formData.get('technique');
    let body = {
        start_date: start_date,
        finish_date: finish_date,
        technique: technique
    };
    await fetch('/~s338923/isbd/backend.php', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(body)
      }).then(() => {
        alert("Подписка оформлена!")
        location.reload()
      });
}

// отправка формы с расписанием
async function schedule(event,par){
    event.preventDefault()
    const formElement = document.querySelector(par);
    const formData = new FormData(formElement);
    const days = formData.get('days');
    let body = {
        days: days
    };
    await fetch('/~s338923/isbd/backend.php', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(body)
      }).then(() => {
        alert("Распиание изменено!")
        window.location.href = '/~s338923/isbd/profile.php'
      });
}

// document.addEventListener('DOMContentLoaded', function(event) {
//     switch (event.target.location.pathname){
//     case '/~s338923/isbd/index.php':
//         document.querySelector('.save-order-form').addEventListener('submit', (e) => save_order(e,'.save-order-form'));
//     case '/~s338923/isbd/rate.php':
//         document.querySelector('.rate-order-form').addEventListener('submit', (e) => rate_order(e,'.rate-order-form'));
//     case '/~s338923/isbd/qa.php':
//         document.querySelector('.qa-form').addEventListener('submit', (e) => qa(e,'.qa-form'));
//     case '/~s338923/isbd/subscribe.php':
//         document.querySelector('.subscribe-form').addEventListener('submit', (e) => subscribe(e,'.subscribe-form'));
//     case '/~s338923/isbd/profile.php':
//         document.querySelector('.schedule-form').addEventListener('submit', (e) => schedule(e,'.schedule-form'));
//     }
// });