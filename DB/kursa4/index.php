<?php 
include_once "header.php";
include_once "db.php";
global $db_connect;
?>
<div class='main'>
    <h1>Заказы</h1>
    <div class="btn_block">
        <button class="btn" onclick="showForm(this)">Создать заказ</button>
        <form method='POST' class='save-order-form' id='form'>
            <div class="element">
                <label for="date">Дата, когда приедет мастер</label>
                <input type="date" name='date' required>
            </div>
            <div class="element">
                <label for="comment">Опишите проблему</label>
                <input type="text" name='comment' required>
            </div>
            <div class="element">
                <label for="technique">Выберите прибор</label>
                <select  name="technique" required>
                    <option value="Посудомойка">Посудомойка</option>
                    <option value="Холодильник">Холодильник</option>
                </select>
            </div>
            <input type="submit" class='btn' value="Сохранить">
        </form>
    </div>
    <h2>Активные заказы</h2>
    <div class="list">
        <?php
        // id заказа
        // статус заказа
        // тип техники(название)
        // дата ремонта
        // мастер
        // оплата
        // комментарий
            // $req = $db_connect->query('SELECT id FROM techniqueType where name=:technique');
        
        ?>
        <div class="item">
            <div class='info'>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Мастер: Иванов Иван</p>
                <p>Оплата: Подписка</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
        <div class="item">
            <div class='info'>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Мастер: Иванов Иван</p>
                <p>Оплата: Подписка</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
        <div class="item">
            <div class='info'>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Мастер: Иванов Иван</p>
                <p>Оплата: Подписка</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
        <div class="item">
            <div class='info'>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Мастер: Иванов Иван</p>
                <p>Оплата: Подписка</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
    </div>
    <h2>История заказов</h2>
    <div class="list">
        <div class="item">
            <div class='order'>
                <a href="/~s338923/isbd/rate.php?id=1">Оценить заказ</a>
                <p>Заказ №1 Статус - Выполняется</p>
                <p>Тип техники: Посудомойка</p>
                <p>Дата ремонта: 29.12.2023</p>
                <p>Мастер: Иванов Иван</p>
                <p>Оплата: Подписка</p>
                <p>Комментарий: Протекает посудомойка</p>
            </div>
        </div>
    </div>
</div>
<?php include_once "footer.php";?>
