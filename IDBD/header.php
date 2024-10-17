<?php include_once "db.php";?>
<?php 
    if (!$_COOKIE["USER_ID"] || !$_COOKIE["ROLE"]) {
        header('Location: https://se.ifmo.ru/~s338923/isbd/auth.php');
    }
?>
<!DOCTYPE html>
<html lang="ru">
 
<head>
    <meta charset="UTF-8">
    <title>Сервис ремонта</title>
    <link rel="stylesheet" href="/~s338923/isbd/css/header.css">
    <link rel="stylesheet" href="/~s338923/isbd/css/page.css">
    <link rel="stylesheet" href="/~s338923/isbd/css/modal.css">
</head>
<body>
<div class="header">
    <a href="#default" class="logo">Сервис ремонта</a>
    <div class="header-right">
        <?php
        $url = $_SERVER['REQUEST_URI'];
        $url = explode('?', $url);
        $url = $url[0];
        ?>
        <?php if ($_COOKIE["ROLE"]=="Client"):?>
        <a href="/~s338923/isbd/index.php" <?php if($url == "/~s338923/isbd/index.php"):?>class="active"<?php endif?>>Заказы</a>
        <a href="/~s338923/isbd/qa.php" <?php if($url == "/~s338923/isbd/qa.php"):?>class="active"<?php endif?>>Вопросы</a>
        <a href="/~s338923/isbd/subscribe.php" <?php if($url == "/~s338923/isbd/subscribe.php"):?>class="active"<?php endif?>>Подписка</a>
        <a href="/~s338923/isbd/workers.php" <?php if($url == "/~s338923/isbd/workers.php"):?>class="active"<?php endif?>>Мастера</a>
        <a href="/~s338923/isbd/add.php" <?php if($url == "/~s338923/isbd/add.php"):?>class="active"<?php endif?>>Приборы</a>
        <?php elseif ($_COOKIE["ROLE"]=="Master"):?>
        <a href="/~s338923/isbd/profile.php" <?php if($url == "/~s338923/isbd/profile.php"):?>class="active"<?php endif?>>Профиль мастера</a>
        <a href="/~s338923/isbd/orders.php" <?php if($url == "/~s338923/isbd/orders.php"):?>class="active"<?php endif?>>Заказы мастера</a>
        <?php endif?>
        <a href="/~s338923/isbd/auth.php?exit=y">Выход</a>
    </div>
</div>


