# JobProj_Chat
Desktop chat application.

Current list of commands:
- /change
- /help
- /close

VERSION 2.0

Add:
1. H2DB instead of working with files

TODO:
1. <del>Отдельный компонент, отвечающий за DB, вместо FileWork</del>
2. <del>Проверить, что FileWork используется только как класс, и не связан с другими классами</del>
3. <del>Удалить FileWork, полностью интегрировать работу с DB</del>
4. <del>Использовать maven для сборки проекта</del>

//==//==//==//==//==//==//==//==

VERSION 1.0

Chat application that works using console and supportive files. After writing any text in console program should answer using any text from helping file.

1. First file's string should be used once on startup. Something like "welcome".
2. Last file's string also just once when program closing. "Goodbye"
3. Add into chat console commands:
    - to switch helping files with answers
    - to close program.
    
TO DO:
1. <del>Отладить выполнение команды смены файла.</del>
2. <del>Отладить работу программы при вводе пустой строки</del>
3. Подумать над умным ответом программы (?)
4. <del>при выборе другой директории часто выпадает та же самая</del>
5. <del>Поправить оформление</del>
6. <del>Добавить отделение строк при выводе ответа</del>
7. <del>Добавить общие команды для классов пользовательского интерфейса (без информации о способе ввода-вывода)</del>
8. Обработка ошибок (???)
9. Осторожная работа с памятью (?) - не записывать вывод в один String.