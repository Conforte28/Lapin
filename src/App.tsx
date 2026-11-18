/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

export default function App() {
  return (
    <div className="min-h-screen bg-[#f5faf7] text-[#1f3528] py-10 px-4 text-[13px]">
      <div className="max-w-3xl mx-auto bg-white border border-[#d6e7dd] border-t-3 border-t-[#558f6f] p-6 sm:p-8 shadow-md">
        <div className="border-b border-[#d6e7dd] pb-4 mb-5 flex flex-wrap items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <img
              src="/assets/aistudio/651825746073557841_95ef30e7.jpg"
              alt="Logo AZWI"
              className="w-11 h-11 object-cover border border-[#c5dfd0] bg-white p-1 shadow-xs"
            />
            <div>
              <h1 className="text-lg font-bold tracking-wider text-[#1b3828]">
                AZWI — Version 2.0
              </h1>
              <p className="text-xs text-[#546e5f]">
                Suivi de production de lapins (Java 17 / Spring Boot 3 / PostgreSQL)
              </p>
            </div>
          </div>
          <a
            href="/suivi-lapins.zip"
            download="suivi-lapins.zip"
            className="inline-block bg-[#558f6f] hover:bg-[#44795c] text-white font-semibold text-xs px-4 py-2 rounded-lg shadow-sm"
          >
            Télécharger suivi-lapins.zip
          </a>
        </div>

        <div className="space-y-3 text-xs leading-relaxed text-[#314b3c]">
          <p>
            La version 2.0 est prête dans <code className="font-mono bg-[#eaf4ee] px-1.5 py-0.5">suivi-lapins/</code> et sera compilée directement par le <code className="font-mono bg-[#eaf4ee] px-1.5 py-0.5">Dockerfile</code> à la racine.
          </p>
          <p>
            Envoyez la mise à jour vers votre dépôt GitHub pour redéployer sur Render sans modifier vos variables d&apos;environnement ni votre base PostgreSQL.
          </p>
        </div>
      </div>
    </div>
  );
}
