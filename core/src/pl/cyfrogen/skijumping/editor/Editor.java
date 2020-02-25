package pl.cyfrogen.skijumping.editor;

public interface Editor {
    void addSlider(String name, float minValue, float maxValue, float currentValue, OnSliderChanged onSliderChanged);

    void addColorChanged(String name, String actualValue, OnColorChanged onColorChanged);
    void addCheckbox(String name, boolean state, OnCheckboxChanged onCheckboxChanged);
}
