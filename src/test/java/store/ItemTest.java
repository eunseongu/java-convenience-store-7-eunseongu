package store;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import store.console.InputHandler;
import store.console.InputValidator;
import store.product.ProductInventory;
import store.util.ErrorMessage;


class ItemTest {
    private final ProductInventory productInventory = new ProductInventory();
    private final InputValidator inputValidator = new InputValidator(productInventory);
    private final InputHandler inputHandler = new InputHandler(inputValidator);

    @Test
    void 구매_정보_입력이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @Test
    void 구매_정보_입력의_대괄호_형식이_유효하지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-3,[[에너지바-5]]]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @Test
    void 구매_정보_입력의_하이픈_형식이_유효하지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라--3],[에너지바5]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @Test
    void 구매_수량이_숫자가_아니면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-한 개],[에너지바-a]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @Test
    void 구매_수량이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-],[에너지바-  ]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @Test
    void 구매_수량이_0이면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-0]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @Test
    void 상품명이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[-3],[ -5]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

}
