<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход</title>
    <link rel="stylesheet" href="/~s338923/isbd/css/auth.css">
</head>
<body>
<div class="registration-cssave">
    <?php
        if ($_GET['exit'] && $_GET['exit'] == 'y') {
            setcookie("USER_ID", '');
            setcookie("ROLE", '');
        }
        elseif ($_COOKIE["USER_ID"] && $_COOKIE["ROLE"]) {
            $page = $_COOKIE["ROLE"] == "Client" ? 'index' : 'orders';
            header('Location: https://se.ifmo.ru/~s338923/isbd/'. $page .'.php');
        }
        if ($_POST['username'] && $_POST['password']) {
            $pass = true;
            $login = true;
            if ($_POST['username'] == '') {
                echo 'Не заполнено поле логин';
                $login = false;
            }
            if ($_POST['password'] == '') {
                echo 'Не заполнено поле пароль';
                $pass = false;
            }
            if ($login && $pass) {
                include_once "db.php";
                $stmt = $db_connect->prepare('SELECT check_password(:login, :password)');

                // bind value to the :id parameter
                $stmt->bindValue(':login', $_POST['username']);
                $stmt->bindValue(':password', $_POST['password']);
                // execute the statement
                $stmt->execute();

                // return the result set as an object
                $obj = $stmt->fetch(\PDO::FETCH_ASSOC);
                
                if ($obj['check_password']) {
                    $obj['check_password'] = trim($obj['check_password'], '()');
                    $ar = explode(",",$obj['check_password']);
                    setcookie("USER_ID", $ar[0]);
                    setcookie("ROLE", $ar[1]);
                    $page = $ar[1] == "Client" ? 'index' : 'orders';
                    header('Location: https://se.ifmo.ru/~s338923/isbd/'. $page .'.php');
                } else {
                    echo "Неправильный логин или пароль";
                }
            }
        }
    ?>
    <form method="post">
        <h3 class="text-center">Форма входа</h3>
        <div class="form-group">
            <input class="form-control item" type="text" name="username" maxlength="20" minlength="4" pattern="^[a-zA-Z0-9_.-]*$" id="username" placeholder="Логин" required>
        </div>
        <div class="form-group">
            <input class="form-control item" type="password" name="password" minlength="1" id="password" placeholder="Пароль" required>
        </div>
        <div class="form-group">
            <button class="btn btn-primary btn-block create-account" type="submit">Вход в аккаунт</button>
        </div>
    </form>
</div>
</body>
</html>