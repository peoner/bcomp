/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum ControlSignal {
	/**
	 * Halt (0)
	 */
	HALT,
	/**
	 * РД -> Правый вход АЛУ (1)
	 */
	DATA_TO_ALU,
	/**
	 * РК -> Правый вход АЛУ (2)
	 */
	INSTR_TO_ALU,
	/**
	 * СК -> Правый вход АЛУ (3)
	 */
	IP_TO_ALU,
	/**
	 * А -> Левый вход АЛУ (4)
	 */
	ACCUM_TO_ALU,
	/**
	 * РС -> Левый вход АЛУ (5)
	 */
	STATE_TO_ALU,
	/**
	 * КлР -> Левый вход АЛУ (6)
	 */
	KEY_TO_ALU,
	/**
	 * Левый вход: инверсия (7)
	 */
	INVERT_LEFT,
	/**
	 * Правый вход: инверсия (8)
	 */
	INVERT_RIGHT,
	/**
	 * АЛУ: + или & (9)
	 */
	ALU_AND,
	/**
	 * АЛУ: +1 (10)
	 */
	ALU_PLUS_1,
	/**
	 * Сдвиг вправо (11)
	 */
	SHIFT_RIGHT,
	/**
	 * Сдвиг влево (12)
	 */
	SHIFT_LEFT,
	/**
	 * БР(16) -> С (13)
	 */
	BUF_TO_STATE_C,
	/**
	 * БР(15) -> N (14)
	 */
	BUF_TO_STATE_N,
	/**
	 * БР == 0 -> Z (15)
	 */
	BUF_TO_STATE_Z,
	/**
	 * 0 -> С (16)
	 */
	CLEAR_STATE_C,
	/**
	 * 1 -> С (17)
	 */
	SET_STATE_C,
	/**
	 * БР -> РА (18)
	 */
	BUF_TO_ADDR,
	/**
	 * БР -> РД (19)
	 */
	BUF_TO_DATA,
	/**
	 * БР -> РК (20)
	 */
	BUF_TO_INSTR,
	/**
	 * БР -> СК (21)
	 */
	BUF_TO_IP,
	/**
	 * БР -> А (22)
	 */
	BUF_TO_ACCUM,
	/**
	 * Память -> РД (23)
	 */
	MEMORY_READ,
	/**
	 * РД -> Память (24)
	 */
	MEMORY_WRITE,
	/**
	 * Ввод-вывод (25)
	 */
	INPUT_OUTPUT,
	/**
	 * Очистка всех флагов (26)
	 */
	CLEAR_ALL_FLAGS,
	/**
	 * DI (27)
	 */
	DISABLE_INTERRUPTS,
	/**
	 * EI (28)
	 */
	ENABLE_INTERRUPTS
}
