package ru.pupov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.pupov.appcontainer.AppComponentsContainerImpl;
import ru.pupov.config.AppConfig;
import ru.pupov.services.*;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {"GameProcessor, ru.pupov.services.GameProcessor",
            "GameProcessorImpl, ru.pupov.services.GameProcessor",
            "gameProcessor, ru.pupov.services.GameProcessor",

            "IOService, ru.pupov.services.IOService",
            "IOServiceConsole, ru.pupov.services.IOService",
            "ioService, ru.pupov.services.IOService",

            "PlayerService, ru.pupov.services.PlayerService",
            "PlayerServiceImpl, ru.pupov.services.PlayerService",
            "playerService, ru.pupov.services.PlayerService",

            "EquationPreparer, ru.pupov.services.EquationPreparer",
            "EquationPreparerImpl, ru.pupov.services.EquationPreparer",
            "equationPreparer, ru.pupov.services.EquationPreparer"
    })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component = null;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.pupov.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        for (var field: component.getClass().getFields()){
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class, EquationPreparer.class);
        }

    }
}