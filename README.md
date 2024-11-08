# java-convenience-store-precourse

# 프리코스 4주차

# 🎰 편의점

## 프로젝트 목적

**구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.**

## 요구 사항

### 규칙

> - 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.
>   - 총구매액은 상품별 가격과 수량을 곱하여 계산한다.
>   - 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
> - 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
>   - 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
>   - 구현에 필요한 상품 목록과 행사 목록 형식을 유지한다면 값을 수정할 수 있다.

### 요구사항

#### 재고 관리
> - 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
> - 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감한다. 
>   - 최신 재고 상태를 유지하여 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.

#### 프로모션 할인
> - 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
> - 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
> - 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용된다.
> - 동일 상품에 여러 프로모션이 적용되지 않는다.
> - 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
> - 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
> - [안내 출력] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
> - [안내 출력] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.

#### 멤버십 할인
> - 멤버십 회원은 "프로모션 미적용 금액"의 30%를 할인받는다.
> - 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
> - 멤버십 할인의 최대 한도는 8,000원이다.

#### 영수증 출력
> - 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
>   - 구매 상품 내역: 구매한 상품명, 수량, 가격
>   - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
>   - 금액 정보
>     - 총구매액: 구매한 상품의 총 수량과 총 금액
>     - 행사할인: 프로모션에 의해 할인된 금액
>     - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
>     - 내실돈: 최종 결제 금액

### 예외 처리

> - 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException을 발생시킨다.
> - 예외 상황 시 "[ERROR]"로 시작하는 에러 메시지를 출력 한다.
> - 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.

### 진행 과정
1. 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 
   - 만약 재고가 0개라면 재고 없음을 출력한다.
2. 프로모션 상품을 구매한 경우 아래 상황에 해당되면 안내 메시지를 출력한다. 
   1. 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우 
     - > 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
   2. 프로모션 재고가 부족하여 혜택 없이 결제해야 하는 경우
     - > 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
3. 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
4. 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
5. 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.

## 기능 목록
### 💡사용자 입력 처리

- 사용자로부터 구매할 상품과 수량을 입력 받는다.
  - 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
  - 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`을 발생시키며 입력을 다시 받는다.
      - 입력이 비어있는 경우 
      - 존재하지 않는 상품을 입력한 경우
      - 구매할 상품과 수량 형식이 올바르지 않은 경우(하이픈, 대괄호, 쉼표 구분이 유효하지 않은 경우)

    
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
    - Y: 증정 받을 수 있는 상품을 추가한다.
    - N: 증정 받을 수 있는 상품을 추가하지 않는다.


- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
    - Y: 일부 수량에 대해 정가로 결제한다.
    - N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.


- 멤버십 할인 적용 여부를 입력 받는다.
  - Y: 멤버십 할인을 적용한다. 
  - N: 멤버십 할인을 적용하지 않는다.

    
- 추가 구매 여부를 입력 받는다. 
  - Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다. 
  - N: 구매를 종료한다.


>- Y, N 입력에 대한 예외
>  - 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`을 발생시키며 입력을 다시 받는다. 
>    - 입력이 비어있는 경우 
>    - Y 또는 N이 아닌 경우


### 💡재고 관리

- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
  - 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`을 발생시키며 입력을 다시 받는다.
    - 구매 수량이 재고 수량을 초과한 경우
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.


### 💡프로모션 관리
- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
- 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
  - 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
- 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
  - 프로모션 재고를 우선적으로 차감하며, 부족할 경우에는 일반 재고를 사용한다.
  - 일부 수량에 대해 정가로 결제하게 됨을 안내한다.


### 💡멤버십 계산기
- 프로모션 미적용 금액의 30%를 할인한다.
- 멤버십 할인의 최대 한도는 8,000원이다.


### 💡금액 계산기
- 영수증 출력에 필요한 가격을 계산한다. 
  - 총구매액: 구매한 상품의 총 수량과 총 금액
  - 행사할인: 프로모션에 의해 할인된 금액
  - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
  - 내실돈: 최종 결제 금액
    - 내실돈 = 총 구매액 - 행사 할인 - 멤버십 할인


### 💡출력
- 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 
  - 재고가 0개라면 재고 없음을 출력한다.
- 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
