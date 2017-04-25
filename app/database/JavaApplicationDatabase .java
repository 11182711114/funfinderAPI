package database;

import javax.inject.Inject;

import play.mvc.Controller;
import play.db.NamedDatabase;
import play.db.Database;

class JavaApplicationDatabase  extends Controller {
    private Database db;

    @Inject
    public JavaNamedDatabase(Database db) {
        this.db = db;
    }

    
}