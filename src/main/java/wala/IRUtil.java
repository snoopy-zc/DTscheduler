package wala;

import java.util.Iterator;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSAInstruction;

public class IRUtil {

	public static BasicBlock getBasicBlock(CGNode cgNode, int bbNum) {
		IR ir = cgNode.getIR();
		SSACFG cfg = ir.getControlFlowGraph();
		return cfg.getBasicBlock(bbNum);
	}

	public static int getSSAIndex(CGNode cgNode, SSAInstruction ssa) {
		IR ir = cgNode.getIR();
		SSAInstruction[] ssas = ir.getInstructions();
		int index = -1;
		for (int i = 0; i < ssas.length; i++)
			if (ssas[i] != null)
				if (ssas[i].equals(ssa)) {
					index = i;
					break;
				}
		return index;
	}

	public static int getSourceLineNumberFromBB(CGNode cgNode, ISSABasicBlock bb) {
		for (Iterator<SSAInstruction> it = bb.iterator(); it.hasNext();) {
			SSAInstruction ssa = it.next();
			int linenum = getSourceLineNumberFromSSA(cgNode, ssa);
			if (linenum != -1)
				return linenum;
		}
		return -1;
	}

	public static int getSourceLineNumberFromSSA(CGNode cgNode, SSAInstruction ssa) {
		IR ir = cgNode.getIR();
		IBytecodeMethod bytecodemethod = (IBytecodeMethod) ir.getMethod();

		int index = getSSAIndex(cgNode, ssa);
		if (index != -1) {
			try {
				int bytecodeindex = bytecodemethod.getBytecodeIndex(index);
				int sourcelinenum = bytecodemethod.getLineNumber(bytecodeindex);
				if (sourcelinenum != -1)
					return sourcelinenum;
			} catch (InvalidClassFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}

}