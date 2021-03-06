package ru.job4j.tracker;


import java.util.ArrayList;
import java.util.List;

/**
 * Class for select action.
 *
 * @author gkuznetsov.
 * @since 20.09.17.
 * @version 0.1.
 */
public class MenuTracker {
    /**
     * Input.
     */
    private Input input;
    /**
     * Item storage.
     */
    private Tracker tracker;
    /**
     * Action storage.
     */
    private ArrayList<UserAction> actions = new ArrayList<>();
    /**
     * Operation position in array.
     */
    //private int position = 0;

    /**
     * Return size of array operation.
     * @return int.
     */
    public int getMenuSize() {
        return actions.size();
    }
    /**
     * Constructor.
     *
     * @param input   - input.
     * @param tracker - input storage.
     */
    public MenuTracker(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * Fill the array storage with data.
     */
    public void fillActions() {
        this.actions.add(new AddItem("Add item", 0));
        this.actions.add(new ShowAllItems("Show all items", 1));
        this.actions.add(new EditItem("Edit item", 2));
        this.actions.add(new DeleteItem("Delete item", 3));
        this.actions.add(new FindItemByName("Find items by name", 4));
//        this.actions.add(new FindItemByID("Find item by ID", position));

    }

    /**
     * Add new action.
     * @param action - action.
     */
    public void addAction(UserAction action) {
        this.actions.add(action);
    }

    /**
     * Take index of action and return action.
     *
     * @param key - index of action.
     */
    public void select(int key) {
        this.actions.get(key).execute(this.input, this.tracker);
    }

    /**
     * Print menu items.
     */
    public void showMenu() {
        for (UserAction action : actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }

    /**
     * Inner class for add item.
     */
    private class AddItem extends BaseAction {
        /**
         * Operation number.
         */
        private int key;

        /**
         * Constructor.
         * @param name - operation name.
         * @param key - operation number.
         */
        AddItem(String name, int key) {
            super(name, key);
            this.key = key;
        }

        /**
         * Get operation index.
         *
         * @return int.
         */
        @Override
        public int key() {
            return this.key;
        }

        /**
         * Add item in item storage.
         *
         * @param input   - input.
         * @param tracker - input storage.
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            String name = input.ask("Pleas enter user name");
            String description = input.ask("Pleas enter item description");
            String[] comments = input.ask("Pleas enter item comments").split(";");
            Item item = tracker.add(new Item(name, description, comments));
            if (item != null) {
                input.print("Item " + item.getName() + " was added\n");
            } else {
                input.print("Error! Item " + item.getName() + "was not added!!!\n");
            }
        }
    }

    /**
     * Inner class for show all items.
     */
    private class ShowAllItems extends BaseAction {
        /**
         * Operation number.
         */
        private int key;

        /**
         * Constructor.
         * @param name - operation name.
         * @param key - operation number.
         */
        ShowAllItems(String name, int key) {
            super(name, key);
            this.key = key;
        }

        /**
         * Get operation index.
         *
         * @return int.
         */
        @Override
        public int key() {
            return this.key;
        }

        /**
         * Add item in item storage.
         *
         * @param input   - input.
         * @param tracker - item storage.
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            List<Item> items = tracker.findAll();
            if (items.size() == 0) {
                input.print("Does't find any items\n");
            } else {
                input.print("Was find " + items.size() + " items:\n");
            }
            for (Item item : items) {
                input.print(String.format("ID: %s;  Name: %s; Description: %s;", item.getId(), item.getName(), item.getDesc()));
            }
        }

        /**
         * Print operation name in menu.
         *
         * @return String.
         */
        @Override
        public String info() {
            return String.format("%s. %s", this.key(), "Show all items.");
        }
    }

    /**
     * Inner class for edit item.
     */
    private class EditItem extends BaseAction {
        /**
         * Operation number.
         */
        private int key;

        /**
         * Constructor.
         * @param name - operation name.
         * @param key - operation number.
         */
        EditItem(String name, int key) {
            super(name, key);
            this.key = key;
        }

        /**
         * Get operation index.
         *
         * @return int.
         */
        @Override
        public int key() {
            return this.key;
        }

        /**
         * Edit selected item.
         *
         * @param input   - input.
         * @param tracker - input storage.
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            String id = input.ask("Enter item ID");
            Item item = tracker.findById(id);
            if (item == null) {
                input.print("Item not found\n");
            } else {
                String newName = input.ask("Enter new user name");
                String newDesc = input.ask("Enter new item description");
                String[] comments = input.ask("Enter new item comments").split(";");
                Item newItem = new Item(newName, newDesc, comments);
                newItem.setId(item.getId());
                tracker.update(newItem);
            }
        }

        /**
         * Print operation name in menu.
         *
         * @return String.
         */
        @Override
        public String info() {
            return String.format("%s. %s", this.key(), "Edit item.");
        }
    }

    /**
     * Inner class for delete item.
     */
    private class DeleteItem extends BaseAction {
        /**
         * Operation number.
         */
        private int key;
        /**
         * Constructor.
         * @param name - operation name.
         * @param key - operation number.
         */
        DeleteItem(String name, int key) {
            super(name, key);
            this.key = key;
        }

        /**
         * Get operation index.
         *
         * @return int.
         */
        @Override
        public int key() {
            return this.key;
        }

        /**
         * Add item in item storage.
         *
         * @param input - input.
         * @param tracker - input storage.
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            String id = input.ask("Enter item ID which you want delete: ");
            Item deleteItem = tracker.findById(id);
            if (deleteItem == null) {
                input.print("Item not found\n");
            } else {
                tracker.delete(deleteItem);
            }
        }

        /**
         * Print operation name in menu.
         *
         * @return
         */
        @Override
        public String info() {
            return String.format("%s. %s", this.key(), "Delete item.");
        }
    }

    /**
     * Inner class for searching item by name.
     */
    private class FindItemByName extends BaseAction {
        /**
         * Operation number.
         */
        private int key;
        /**
         * Constructor.
         * @param name - operation name.
         * @param key - operation number.
         */
        FindItemByName(String name, int key) {
            super(name, key);
            this.key = key;
        }
        /**
         * Get operation index.
         *
         * @return int.
         */
        @Override
        public int key() {
            return this.key;
        }

        /**
         * Search item by Name.
         *
         * @param input   - input.
         * @param tracker - input storage.
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            String name = input.ask("Enter user name: ");
            List<Item> items = tracker.findByName(name);
            if (items.size() == 0) {
                input.print("Item not found\n");
            } else {
                input.print("Was founded " + items.size() + " items: ");
                for (Item item : items) {
                    input.print(String.format("ID: %s;  Name: %s; Description: %s;", item.getId(), item.getName(), item.getDesc()));
                }
            }
        }

        /**
         * Print operation name in menu.
         *
         * @return String.
         */
        @Override
        public String info() {
            return String.format("%s. %s", this.key(), "Find item by Name");
        }
    }

    /**
     * Inner class for item searching by ID.
     */
    private class FindItemByID extends BaseAction {
        /**
         * Operation number.
         */
        private int key;
        /**
         * Constructor.
         * @param name - operation name.
         * @param key - operation number.
         */
        FindItemByID(String name, int key) {
            super(name, key);
            this.key = key;
        }
        /**
         * Get operation index.
         *
         * @return int.
         */
        @Override
        public int key() {
            return  this.key;
        }

        /**
         * Search item by ID.
         *
         * @param input   - input.
         * @param tracker - input storage.
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            String id = input.ask("Enter item ID: ");
            Item item = tracker.findById(id);
            if (item == null) {
                input.print("Item not found\n");
            } else {
                input.print("Was founded:\n" + "ID: " + item.getId() + "; Name: " + item.getName() + "; Description: " + item.getDesc() + ";");
            }
        }

        /**
         * Print operation name in menu.
         *
         * @return String.
         */
        @Override
        public String info() {
            return String.format("%s. %s", this.key(), "Find item by ID.");
        }
    }



}