package store;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;


class ItemTest {
    private final InputHandler inputHandler = new InputHandler();

    @Test
    void 구매_정보_입력이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EMPTY_INPUT.getMessage());
    }

    @Test
    void 구매_정보_입력의_대괄호_형식이_유효하지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-3,[[에너지바-5]]]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.FORMAT_BRACKETS.getMessage());
    }

    @Test
    void 구매_정보_입력의_하이픈_형식이_유효하지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라--3],[에너지바5]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.FORMAT_HYPHEN.getMessage());
    }

    @Test
    void 구매_수량이_숫자가_아니면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-한 개],[에너지바-a]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_ITEM_QUANTITY.getMessage());
    }

    @Test
    void 구매_수량이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-],[에너지바-  ]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EMPTY_ITEM_QUANTITY.getMessage());
    }

    @Test
    void 구매_수량이_0이면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[콜라-0]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.ITEM_QUANTITY_CANNOT_BE_ZERO.getMessage());
    }

    @Test
    void 상품명이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> inputHandler.parseItemInput("[-3],[ -5]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EMPTY_ITEM_NAME.getMessage());
    }

}
