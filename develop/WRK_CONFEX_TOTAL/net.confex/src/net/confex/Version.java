/*******************************************************************************
 * Copyright (c) 2006,2008 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex;

import org.eclipse.core.resources.IResource;

/**
 * 
 * ver 0.2.30 27-Dec-2006
 * 
 * ver 0.2.27 26-Dec-2006
 * 		+ Локализация почти закончена  
 *        
 * ver 0.2.26 22-Dec-2006
 * 		+ добавлена работа с Формами  
 *        org.eclipse.ui.forms
 * 
 * ver 0.2.22  19-Dec-2006
 * 		+ Проведена частичная локализация как в 
 * 		http://www.rssowl.org/	 
 * 
 * ver 0.2.7  11-Dec-2006
 *        
 * ver 0.2.1  04-Dec-2006
 * 		+ BookmarkNode теперь синхронизирован и привязан к 
 * 		  bookmark эклипса. Все изменения происходящие с файлом
 *        будут отслеживаться и сохраняться
 * 
 * ver 0.1.10  02-Dec-2006
 * 		+ Включена внутренняя консоль ConfexConsole
 *        потоки вывода переключены на нее
 * 
 * ver 0.1.9  01-Dec-2006
 * 		+ Добавлены интерфейсы IStateOserver и IStateOservable  
 *        Теперь каждый узел может сообщать о своем состоянии
 *        наблюдателю (NavigationViewer)
 *         
 * ver 0.1.7  01-Dec-2006
 * 		+ Теперь в случае невозможности установления класса 
 *        узла он сохраняется как есть 
 *
 * ver 0.1.5  30-Nov-2006
 * 		+ Создан вариант для работы в составе IDE 
 * 		класс net.confex.editor.editors/ConfexEditor.java
 * 
 *
 * ver 0.1.3  27-Nov-2006
 * 
 * ver 0.0.8.0  21-Nov-2006
 *    + Создано три проекта net.confex.db, 
 *    net.confex.db.ora и net.confex.db.mysql
 *   
 * ver 0.0.7.0  21-Nov-2006
 *    + в net.confex.db добавлена работа MySql через JDBC
 *   
 * ver 0.0.4.0  14-Nov-2006
 *    + Созданы команды (Копирования вставки удаления...) 
 *      и ассоциированы с горячими клавишами
 *    + Операции расширены для множественного выбора  
 *   
 * ver 0.0.2.4  13-Nov-2006  
 *    + Копировать вставлять из буфера обмена теперь можно
 *      по горячим клавишам CTRL+C CTRL+V
 *     
 * ver 0.0.2.3  09-Nov-2006  
 *    + узлы теперь можно перемещать по дереву мышкой 
 * 
 * ver 0.0.2  07-Nov-2006  
 *    + новый формат .confex файла
 *       <properties>
 *       ...
 *       </properties>
 *    + старый формат 0.0.1 поддерживается по умолчанию.  
 *
 * 
 * DONE: Вставить иконку в пункт выпадающего меню "Добавить"
 * DONE: Подключить вывод на консоль
 * DONE: Убрать windows-1251
 * 
 * TODO: Удалить редактор после загрузки вида 
 * NOT TESTED: Оставлять сохранение как есть для неопознанных классов узлов
 * DONE: Сделать возможность блокирования узлов на изменение
 * DONE: Сделать подключения узлов из других файлов
 * DONE: Сделать создание новых маркеров эклипса в случае их отсутствия
 * 
 * DONE: Убрать  HORIZONTAL_ALIGN_FILL и VERTICAL_ALIGN_FILL
 * DONE: Удалить из Видов clipboard 
 * 
 * TODO: Убрать обновление всего дерева при получении фокуса одним элементом
 * 
 * TODO: Сделать так чтобы иконки брались из своего проекта (dbexplorer16x16x32.png)
 *  
 * FIXME: Не допускать повторной загрузки config файла!!!
 * DONE: Не допускать повторного открытия диалога свойств!!!  
 * 
 * FIXME: Сделать переподключение коннекции к базе "Connection reset by peer: socket write error" 
 * 
 * TODO: Удалить BookmarkImpl!
 * TODO: Во время открытия Конфекс файлов сделать удaление пустого вида в случае отмены загрузки 
 *
 * 
 * ver 0.9.5 2-Nov-2007
 *    + при выполнении groovy файла в classpath включается 
 *      /classes (каталог относительно каталога Конфекс файла) 
 *	    в каталог /lib можно складывать jar библиотеки явы	
 * 
 * В Utils.openExternalFile()
 * 
 * 		if (existing == null)
 *			i_file.createLink(location, IResource.NONE, null);
 *		else
 *			i_file.createLink(location, IResource.REPLACE, null);
 *	
 *	в каталог /lib можно складывать jar библиотеки явы	
 *
 *
 * TODO: Сделать однотипной загрузку исходных текстов для GroovyNode, HtmlTextNode, JavaNode,   
 * 
 * ver 0.9.6 9-Nov-2007
 *    + добавлена возможность генерации html из groovy скрипта
 *    = при создании нового узла при нажатии Cancel теперь не остается узел-пустышка    
 *
 *    + запуск на выполнение узла из груви скрипта
 *    
 * ver 0.9.7 13-Nov-2007
 * 	  - будем пробовать переделать все run методы чтобы обойтись без NavigationView 	
 *    
 *    
 * TODO: Remove dependency of net.confex from org.eclipse.ide!   
 *    
 */

public class Version {

	public static String getVersion() {
		return "0.9.17";
	}
	
}
