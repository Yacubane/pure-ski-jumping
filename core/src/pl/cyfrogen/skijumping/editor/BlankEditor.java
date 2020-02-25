package pl.cyfrogen.skijumping.editor;

public class BlankEditor implements Editor {
    @Override
    public void addSlider(String name, float minValue, float maxValue, float currentValue, OnSliderChanged onSliderChanged) {

    }

    @Override
    public void addColorChanged(String name, String actualValue, OnColorChanged onColorChanged) {

    }

    @Override
    public void addCheckbox(String name, boolean state, OnCheckboxChanged onCheckboxChanged) {

    }
}
