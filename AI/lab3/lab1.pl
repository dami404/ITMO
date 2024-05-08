:- discontiguous 
    mother/2, child/2,father/2, brother/2,dating/2.

% facts
male('Крис Хэкетт').
male('Макс').
male('Ник').
male('Райан').
male('Джейкоб').
male('Дилан').
male('Тревис Хэкетт').
male('Бобби Хэкетт').
male('Джедайа Хэкетт').
male('Сайлас').


female('Лаура').
female('Эбигейл').
female('Кейтлин').
female('Эмма').
female('Элиза').
female('Констанс Хэкетт').


counselor('Макс').
counselor('Ник').
counselor('Райан').
counselor('Джейкоб').
counselor('Дилан').
counselor('Лаура').
counselor('Эбигейл').
counselor('Кейтлин').
counselor('Эмма').


% кого превратили / кто превратил
werewolf('Сайлас', 'Элиза').
werewolf('Ник', 'Крис Хэкетт').
werewolf('Крис Хэкетт', 'Сайлас').
werewolf('Макс', 'Сайлас').
werewolf('Лаура', 'Макс').
werewolf('Райан', 'Лаура').
werewolf('Эмма', 'Макс').
werewolf('Джейкоб', 'Крис Хэкетт').
werewolf('Кейтлин', 'Сайлас').
werewolf('Дилан', 'Кейтлин').



human('Тревис Хэкетт').
human('Бобби Хэкетт').
human('Джедайа Хэкетт').
human('Констанс Хэкетт').
human('Эбигейл').

ghost('Элиза').


parent('Констанс Хэкетт','Крис Хэкетт').
parent('Констанс Хэкетт','Тревис Хэкетт').
parent('Констанс Хэкетт','Бобби Хэкетт').
parent('Джедайа Хэкетт','Крис Хэкетт').
parent('Джедайа Хэкетт','Тревис Хэкетт').
parent('Джедайа Хэкетт','Бобби Хэкетт').
parent('Элиза','Сайлас').


has_silver_bullet('Бобби Хэкетт').
has_silver_bullet('Тревис Хэкетт').
has_silver_bullet('Джедайа Хэкетт').
has_silver_bullet('Лаура').
has_silver_bullet('Кейтлин').
has_silver_bullet('Райан').

% кто убивает/кого
kills('Бобби Хэкетт','Джейкоб').
kills('Тревис Хэкетт','Лаура').
kills('Лаура','Сайлас').
kills('Лаура','Крис Хэкетт').
kills('Кейтлин','Макс').
kills('Райан','Ник').

%rules

mother(Mother, Child) :- female(Mother), parent(Mother, Child).
father(Father, Child) :- male(Father), parent(Father, Child).
dating(Person1, Person2) :- Person1 \= Person2, dating(Person2, Person1).
brother(Person1,Person2) :- male(Person1), male(Person2), child(Person1,Parent), child(Person2,Parent). 
child(Child, Parent) :- parent(Parent, Child).

printDating(Person, Friend) :- dating(Person, Friend); dating(Friend,Person).

printAllSiblings(Person, Sibling) :-mother(Sibling, Person);father(Sibling, Person);brother(Sibling, Person); child(Sibling, Person).

findKills(Killer,Deads) :- findall(Dead, kills(Killer, Dead), Deads).

monsterChecker(Victim,Monster) :- werewolf(Victim,Monster).
findVictims(Monster,Victims) :- findall(Victim, werewolf(Victim,Monster), Victims).

%predicates

mother('Констанс Хэкетт', 'Тревис Хэкетт').
mother('Констанс Хэкетт', 'Бобби Хэкетт').
mother('Констанс Хэкетт', 'Крис Хэкетт').

mother('Элиза', 'Сайлас').


father('Джедайа Хэкетт', 'Тревис Хэкетт').
father('Джедайа Хэкетт', 'Бобби Хэкетт').
father('Джедайа Хэкетт', 'Крис Хэкетт').


dating('Эмма','Джейкоб').
dating('Лаура','Макс').

dating('Джейкоб','Эмма').
dating('Эбигейл','Ник').
dating('Ник','Эбигейл').
dating('Дилан','Кейтлин').
dating('Кейтлин','Дилан').


brother('Тревис Хэкетт','Бобби Хэкетт').
brother('Бобби Хэкетт','Тревис Хэкетт').

brother('Тревис Хэкетт','Крис Хэкетт').
brother('Крис Хэкетт','Тревис Хэкетт').

brother('Крис Хэкетт','Бобби Хэкетт').
brother('Бобби Хэкетт','Крис Хэкетт').


child('Тревис Хэкетт','Джедайа Хэкетт').
child('Крис Хэкетт','Джедайа Хэкетт').
child('Бобби Хэкетт','Джедайа Хэкетт').

child('Тревис Хэкетт','Констанс Хэкетт').
child('Крис Хэкетт','Констанс Хэкетт').
child('Бобби Хэкетт','Констанс Хэкетт').

child('Сайлас','Элиза').

% 1 - human('Констанс Хэкетт').
%     father('Джедайа Хэкетт', 'Крис Хэкетт').
%
% 2 - werewolf(X, 'Сайлас'),  female(X).
%     not(dating('Эмма',X)); ghost(X); human(X).
%     parent(X,Y), male(X).
%
% 3 - (child(X, 'Элиза');brother('Тревис Хэкетт',X);human(X);female(X)),not(counselor(X)), ghost(X).
%
%
%