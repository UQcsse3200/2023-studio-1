//package com.csse3200.game.ui;
//
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.utils.Align;
//
//public class CoordinatesDisplay extends UIComponent {
//    private Label coordinatesLabel;
//
//    public CoordinatesDisplay() {
//        coordinatesLabel = new Label("", skin);
//        coordinatesLabel.setAlignment(Align.center);
//
//        Table table = new Table();
//        table.setFillParent(true);
//        table.add(coordinatesLabel).expand().center();
//        stage.addActor(table);
//    }
//
//    public void setCoordinates(int x, int y) {
//        coordinatesLabel.setText("X: " + x + "\nY: " + y);
//    }
//
//    @Override
//    public void update(float deltaTime) {
//        // Add any update logic for the coordinates display here
//    }
//}
