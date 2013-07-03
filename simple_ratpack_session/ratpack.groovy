@GrabResolver("https://oss.jfrog.org/artifactory/repo")
@Grab(value = "org.ratpack-framework:ratpack-groovy:0.9.0-SNAPSHOT")

import static org.ratpackframework.groovy.RatpackScript.ratpack
import org.ratpackframework.groovy.templating.TemplateRenderer

// For session handling
import org.ratpackframework.session.Session
import org.ratpackframework.session.store.MapSessionsModule
import org.ratpackframework.session.store.SessionStorage

// You can change anything in the ratpack {} closure without needing to restart

ratpack {

    modules {
        // Enable non persistent map based session storage
        register(new MapSessionsModule(10, 5))
    }
	
    handlers {
        get {
            response.send "This is the app root (also try: /look and /show)"
			
			// add some data into the session using the key 'test'
			get(SessionStorage).test = '1234'
			
			// Add an Object (list of Items) into the session
            def itemList = new ArrayList()

            def item = new Item()
            item.id = 0
            item.name = "Groovy in Action"
            item.itemType = "Book"
            item.itemLocation_1 = "Bookshelf 3"
            item.itemLocation_2 = "Shelf 5"
            item.description = "The best guide to learning Groovy"
            item.createdAt = new Date()

            itemList.add(item)

            def item_2 = new Item()
            item_2.id = 1
            item_2.name = "This is Spinal Tap"
            item_2.itemType = "DVD"
            item_2.itemLocation_1 = "DVD Case 12"
            item_2.itemLocation_2 = "Shelf 2"
            item_2.description = "This one goes to 11"
            item_2.createdAt = new Date()

            itemList.add(item_2)			

			// Add the List Object into the session
			get(SessionStorage).itemData = itemList
        }

        get("look") {
		    // get something out of the session using the key 'test'
		    // get(SessionStorage).test
            response.send "This is the value in the session: ${get(SessionStorage).test}"

            // Retrieve an Object from the session (it is a list of Item objects)			
			def itemList = get(SessionStorage).itemData
			
			//println itemList
			itemList.each() {
			  println "Name is: ${it.name}"
			}
		}
			
		get ("show") {
		
            get(TemplateRenderer).render "show.html", itemList: get(SessionStorage).itemData								
        }

        assets "public"
    }
}